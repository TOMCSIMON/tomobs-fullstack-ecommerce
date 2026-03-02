package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.UserProfileDTO;
import com.tomobs.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    @GetMapping()
    public String getProfile(
            Principal principal,
            Model model) {

        String loggedInEmail = principal.getName();

        UserProfileDTO profileDTO = userService.findByUserByEmail(loggedInEmail);
        model.addAttribute("userDetails", profileDTO);
        return "user-profile";
    }
}
