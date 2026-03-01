package com.tomobs.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserListDTO {

    private Long id;
    private String name;
    private String email;
    private boolean isBlocked;
}
