package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.service.OtpService;
import com.tomobs.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final OtpService otpService;

    @GetMapping("/ajax/check-email")
    public Map<String, Object> checkEmailExists(@RequestParam String email) {
        boolean emailExists = userService.isEmailExists(email);
        Map<String, Object>  response =  new HashMap<>();
        response.put("exists", emailExists);
        if(emailExists) {
            response.put("message", "Email already in use please enter another");
        }
        else {
            response.put("message", "Email available");
        }
        return response;
    }

    @GetMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String email){
        try{
            otpService.resendOtp(email);
            return ResponseEntity.ok().body(
                    Map.of("message", "OTP Send successfully")
            );
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
