package com.tomobs.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingUserRegistration {

    private String userName;
    private String email;
    private String phoneNumber;
    private String encodedPassword;
}