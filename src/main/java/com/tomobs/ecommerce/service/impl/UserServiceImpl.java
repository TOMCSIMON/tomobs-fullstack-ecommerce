package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.*;
import com.tomobs.ecommerce.mapper.UserMapper;
import com.tomobs.ecommerce.model.Role;
import com.tomobs.ecommerce.model.User;
import com.tomobs.ecommerce.enums.RoleEnum;
import com.tomobs.ecommerce.repository.RoleRepository;
import com.tomobs.ecommerce.repository.UserRepository;
import com.tomobs.ecommerce.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserListDTO> listUsers(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> users;
        if (keyword != null && !keyword.trim().isEmpty()) {
            users = userRepository.findByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    keyword, keyword, pageable);
        }else {
            users = userRepository.findAll(pageable);
        }
        return users.map(mapper::toDto);
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not found!"));
        return user;
    }

    @Override
    public void toggleUserBlockStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        user.setBlocked(!user.isBlocked());
        userRepository.save(user);
    }

    @Override
    public UserProfileDTO findByUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        return mapper.toProfileDto(user);
    }

    @Override
    public void updateProfile(String email, ProfileUpdateDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        String field = dto.getField();
        String value = dto.getValue();

        switch (field) {

            case "name":
                user.setUserName(value);
                break;
            case "email":
                if (!email.equals(value) && userRepository.existsByEmail(value)) {
                    throw new RuntimeException("This email is already taken by another user!");
                }
                user.setEmail(value);
                break;
            case "phoneNumber":
                user.setPhoneNumber(value);
                break;
            default:
                throw new RuntimeException("Invalid user profile update Request!");
        }
        userRepository.save(user);
    }

    @Override
    public void updatePassword(String email, ChangePasswordDTO dto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if(!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new RuntimeException("Passwords do not match!");
        }
        if(!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong old password");
        }
        if(passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password cannot be the same as the old password!");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void saveNewPassword(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public long findTotalUsers() {

        return userRepository.countByIsBlockedFalse();
    }
}
