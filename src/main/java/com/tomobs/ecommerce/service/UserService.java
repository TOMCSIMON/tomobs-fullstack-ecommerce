package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.ProfileUpdateDTO;
import com.tomobs.ecommerce.dto.UserListDTO;
import com.tomobs.ecommerce.dto.UserProfileDTO;
import com.tomobs.ecommerce.dto.UserRegistrationDTO;
import com.tomobs.ecommerce.model.User;
import org.springframework.data.domain.Page;

public interface UserService {

//    void registerUser(UserRegistrationDTO userDTO);

    boolean isEmailExists(String email);

    User findByEmail(String email);

    Page<UserListDTO> listUsers(String keyword,int page, int size);

    void toggleUserBlockStatus(Long id);

    UserProfileDTO findByUserByEmail(String email);

    void updateProfile(String email, ProfileUpdateDTO profileUpdateDTO);
}
