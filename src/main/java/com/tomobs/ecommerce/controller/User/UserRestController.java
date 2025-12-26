package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// ALL USER REST ENDPOINTS CAME HERE
@RestController
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/ajax/check-email")
    public Map<String, Object> checkEmailExists(@RequestParam String email) {

        boolean emailExists = userService.isEmailExists(email);

        Map<String, Object>  response =  new HashMap<>();

        response.put("exists", emailExists);

        if(emailExists) {
            response.put("message", "Email already in use please enter another");// do not hardcode in java properties file needed
        }
        else {
            response.put("message", "Email available");
        }

        return response;

    }

}
