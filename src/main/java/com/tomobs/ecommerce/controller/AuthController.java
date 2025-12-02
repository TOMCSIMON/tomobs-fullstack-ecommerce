package com.tomobs.ecommerce.controller;

// HANDLES HTTP REQUESTS FOR USER AUTHENTICATION,RECEIVING SIGNUP FORM DATA
import com.tomobs.ecommerce.dto.UserRegistrationDTO;
import com.tomobs.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

  private final UserService userService;

  public AuthController(UserService userService) {

    this.userService = userService;
  }

  @PostMapping("/signup")
  public String registerUser(
      @ModelAttribute("user") @Valid UserRegistrationDTO userRegistrationDTO,
      BindingResult bindingResult,
      Model model) {

    // SHOW FORM AGAIN WITH VALIDATION ERRORS
    if (bindingResult.hasErrors()) {
      return "/signup";
    }

    userService.registerUser(userRegistrationDTO);
    return "redirect:/login";
  }
}
