package com.tomobs.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpCacheEntry {

    private String otp;
    private PendingUserRegistration user;
}