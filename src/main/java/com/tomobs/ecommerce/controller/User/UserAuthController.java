package com.tomobs.ecommerce.controller.User;

// HANDLES HTTP REQUESTS FOR USER AUTHENTICATION,RECEIVING SIGNUP FORM DATA
import com.tomobs.ecommerce.dto.UserRegistrationDTO;
import com.tomobs.ecommerce.service.OtpService;
import com.tomobs.ecommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserAuthController {

  private final OtpService otpService;

  @PostMapping("/signup")
  public String registerUser(
      @ModelAttribute("user") @Valid UserRegistrationDTO userRegistrationDTO,
      BindingResult bindingResult,
      Model model) {

    if (bindingResult.hasErrors()) {
      return "/signup";
    }
    otpService.generateAndSendOtpForSignup(userRegistrationDTO);
    model.addAttribute("email", userRegistrationDTO.getEmail());
    return "otp-verification";
  }

  @PostMapping("/verify-otp")
  public String verifyOtp(
          @RequestParam("email") String email,
          @RequestParam("otp") String otp,
          Model model) {

    try {
      otpService.verifyOtpAndCreateUser(email, otp);
      return "redirect:/login?verified=true";
    } catch (RuntimeException ex) {
      model.addAttribute("email", email);
      model.addAttribute("otpError", ex.getMessage());
      return "otp-verification";
    }
  }
}
