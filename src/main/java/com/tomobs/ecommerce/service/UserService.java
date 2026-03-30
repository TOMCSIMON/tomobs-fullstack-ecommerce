package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.*;
import com.tomobs.ecommerce.model.User;
import org.springframework.data.domain.Page;

public interface UserService {

    boolean isEmailExists(String email);

    User findByEmail(String email);

    Page<UserListDTO> listUsers(String keyword,int page, int size);

    void toggleUserBlockStatus(Long id);

    UserProfileDTO findByUserByEmail(String email);

    void updateProfile(String email, ProfileUpdateDTO profileUpdateDTO);

    void updatePassword(String email, ChangePasswordDTO dto);

    void saveNewPassword(String email, String password);

    long findTotalUsers();
}
