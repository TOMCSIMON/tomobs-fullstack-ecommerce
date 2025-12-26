package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.UserRegistrationDTO;
import com.tomobs.ecommerce.model.Role;
import com.tomobs.ecommerce.model.User;
import com.tomobs.ecommerce.enums.RoleEnum;
import com.tomobs.ecommerce.repository.RoleRepository;
import com.tomobs.ecommerce.repository.UserRepository;
import com.tomobs.ecommerce.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public void registerUser(UserRegistrationDTO userRegistrationDTO) {

        // CONFIRMS BOTH PASSWORD ENTRIES ARE SAME
        if(!userRegistrationDTO.getPassword().equals(userRegistrationDTO.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match!");
        }


        // CHECKING IF USER EMAIL ALREADY EXISTS IN DB
        if(userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new RuntimeException("Email is Already Registered!");
        }


        // CHANGING USER_DTO TO USER ENTITY
        User user = new User();
        user.setUserName(userRegistrationDTO.getUserName().trim());
        user.setEmail(userRegistrationDTO.getEmail().trim());
        user.setPhoneNumber(userRegistrationDTO.getPhoneNumber().trim());


        // ENCODING THE PASSWORD FROM THE DTO AND SAVES TO THE USER ENTITY
        String encodedPassword = passwordEncoder.encode(userRegistrationDTO.getPassword().trim());
        user.setPassword(encodedPassword);


        // SETTING THE DEFAULT ROLE AS ROLE_USER
        Role defaultRole = roleRepository.findByRoleName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role not found!"));
        user.setRole(defaultRole);


        userRepository.save(user);

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

}
