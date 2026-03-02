package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.ChangePasswordDTO;
import com.tomobs.ecommerce.dto.ProfileUpdateDTO;
import com.tomobs.ecommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileRestController {

    private final UserService userService;

    @PatchMapping("/update")
    public ResponseEntity<?> updateProfile(
            @RequestBody ProfileUpdateDTO profileUpdateDTO,
            Principal principal) {

        String loggedInEmail = principal.getName();

        userService.updateProfile(loggedInEmail, profileUpdateDTO);

        return ResponseEntity.ok(Map.of("success", true));
    }

    @PatchMapping("/password")
    public ResponseEntity<Map<String,Boolean>> changePassword(
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO,
            Principal principal) {

        String email = principal.getName();
        userService.updatePassword(email,changePasswordDTO);
        return ResponseEntity.ok(Map.of("Success", true));
    }
}
