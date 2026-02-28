package com.tomobs.ecommerce.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomobs.ecommerce.dto.OtpCacheEntry;
import com.tomobs.ecommerce.dto.PendingUserRegistration;
import com.tomobs.ecommerce.dto.UserRegistrationDTO;
import com.tomobs.ecommerce.model.Role;
import com.tomobs.ecommerce.model.User;
import com.tomobs.ecommerce.enums.RoleEnum;
import com.tomobs.ecommerce.repository.RoleRepository;
import com.tomobs.ecommerce.repository.UserRepository;
import com.tomobs.ecommerce.service.EmailService;
import com.tomobs.ecommerce.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

  private static final String SIGNUP_KEY_PREFIX = "signup:";
  private static final String ATTEMPT_KEY_PREFIX = "resend_count:";
  private static final Duration OTP_TTL = Duration.ofMinutes(5);

  private final StringRedisTemplate redisTemplate;
  private final ObjectMapper objectMapper;
  private final EmailService emailService;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  @Override
  public void generateAndSendOtpForSignup(UserRegistrationDTO registrationDTO) {

    if (userRepository.existsByEmail(registrationDTO.getEmail().trim())) {
      throw new RuntimeException("Email is already registered!");
    }

    String encodedPassword = passwordEncoder.encode(registrationDTO.getPassword().trim());

    PendingUserRegistration pendingUser =
        new PendingUserRegistration(
            registrationDTO.getUserName().trim(),
            registrationDTO.getEmail().trim(),
            registrationDTO.getPhoneNumber().trim(),
            encodedPassword);

    String otp = String.format("%06d", new Random().nextInt(1_000_000));

    OtpCacheEntry cacheEntry = new OtpCacheEntry(otp, pendingUser);
    String key = SIGNUP_KEY_PREFIX + registrationDTO.getEmail().trim();

    try {
      String json = objectMapper.writeValueAsString(cacheEntry);
      redisTemplate.opsForValue().set(key, json, OTP_TTL);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to save OTP data", e);
    }
    emailService.sendOtpEmail(registrationDTO.getEmail().trim(), otp);
  }

  @Override
  public void resendOtp(String email) {
    String trimmedEmail = email.trim();
    String attemptKey = ATTEMPT_KEY_PREFIX + trimmedEmail;

    String countStr = redisTemplate.opsForValue().get(attemptKey);
    int count = (countStr == null) ? 0 : Integer.parseInt(countStr);

    if (count >= 3) {
      throw new RuntimeException("Exceeded max limit. Please wait for 1 hour");
    }

    String key = SIGNUP_KEY_PREFIX + trimmedEmail;
    String json = redisTemplate.opsForValue().get(key);

    if (json == null) {
      throw new RuntimeException("Session time expired. Please do signup");
    }

    try {

      OtpCacheEntry cacheEntry = objectMapper.readValue(json, OtpCacheEntry.class);

      String newOtp = String.format("%06d", new Random().nextInt(1_000_000));

      cacheEntry.setOtp(newOtp);

      String updatedJson = objectMapper.writeValueAsString(cacheEntry);
      redisTemplate.opsForValue().set(key, updatedJson, OTP_TTL);

      redisTemplate.opsForValue().increment(attemptKey);
      if (count == 0) {
        redisTemplate.expire(attemptKey, Duration.ofHours(1));
      }

      emailService.sendOtpEmail(trimmedEmail, newOtp);

    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed OTP updating");
    }
  }

  @Override
  public void verifyOtpAndCreateUser(String email, String otp) {

    String key = SIGNUP_KEY_PREFIX + email.trim();
    String json = redisTemplate.opsForValue().get(key);

    if (json == null) {
      throw new RuntimeException("OTP expired or not found.");
    }

    try {
      OtpCacheEntry cacheEntry = objectMapper.readValue(json, OtpCacheEntry.class);

      if (!cacheEntry.getOtp().equals(otp)) {
        throw new RuntimeException("Invalid OTP.");
      }

      PendingUserRegistration pending = cacheEntry.getUser();

      if (userRepository.existsByEmail(pending.getEmail())) {
        redisTemplate.delete(key);
        throw new RuntimeException("Email is already registered.");
      }

      User user = new User();
      user.setUserName(pending.getUserName());
      user.setEmail(pending.getEmail());
      user.setPhoneNumber(pending.getPhoneNumber());
      user.setPassword(pending.getEncodedPassword());

      Role role =
          roleRepository
              .findByRoleName(RoleEnum.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Default role not found!"));
      user.setRole(role);

      userRepository.save(user);

      redisTemplate.delete(key);

    } catch (Exception e) {
      throw new RuntimeException("OTP verification failed: " + e.getMessage(), e);
    }
  }
}
