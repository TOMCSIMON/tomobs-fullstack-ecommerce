package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.UserRegistrationDTO;

public interface OtpService {

    void generateAndSendOtpForSignup(UserRegistrationDTO registrationDTO);

    void verifyOtpAndCreateUser(String email, String otp);

    void resendOtp(String email);

    boolean generateAndSendOtpForForgotPassword(String email);

    boolean verifyOtpForgotPassword(String email, String otp);
}