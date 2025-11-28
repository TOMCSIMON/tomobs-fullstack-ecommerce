package com.tomobs.ecommerce.service;

// INTERFACE (ABSTRACTION) DEFINES THE CONTRACT FOR HANDLING USER SIGNUP LOGIC
import com.tomobs.ecommerce.dto.UserRegistrationDTO;

public interface UserService {

    void registerUser(UserRegistrationDTO userDTO);

    boolean isEmailExists(String email);
}
