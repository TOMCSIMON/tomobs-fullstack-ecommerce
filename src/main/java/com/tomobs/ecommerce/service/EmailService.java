package com.tomobs.ecommerce.service;

public interface EmailService {
    void sendOtpEmail(String toEmail, String otp);
}
