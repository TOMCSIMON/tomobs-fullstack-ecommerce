package com.tomobs.ecommerce.controller;

// CONTROLLER FOR HANDLING THE AJAX CALLS
import com.tomobs.ecommerce.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AjaxController {

    private final UserService userService;

    public AjaxController(UserService userService) {

        this.userService = userService;
    }

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
}
