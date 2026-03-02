package com.tomobs.ecommerce.mapper;

import com.tomobs.ecommerce.dto.UserListDTO;
import com.tomobs.ecommerce.dto.UserProfileDTO;
import com.tomobs.ecommerce.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserListDTO toDto(User user) {

        return new UserListDTO(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.isBlocked()
        );
    }

    public UserProfileDTO toProfileDto(User user) {

        return new UserProfileDTO(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }
}
