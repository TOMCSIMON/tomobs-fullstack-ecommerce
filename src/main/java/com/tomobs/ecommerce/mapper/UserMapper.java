package com.tomobs.ecommerce.mapper;

import com.tomobs.ecommerce.dto.UserListDTO;
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
}
