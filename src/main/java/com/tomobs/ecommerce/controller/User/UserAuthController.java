package com.tomobs.ecommerce.controller.User;

// HANDLES HTTP REQUESTS FOR USER AUTHENTICATION,RECEIVING SIGNUP FORM DATA
import com.tomobs.ecommerce.dto.UserRegistrationDTO;
import com.tomobs.ecommerce.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
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

  @PostMapping("/forgot-password/email")
  public String processForgotPassword(
          @RequestParam("email") String email,
          RedirectAttributes redirectAttributes) {

    log.info("FORGOT PASSWORD BACKEND START: {}", email);
    boolean isSent = otpService.generateAndSendOtpForForgotPassword(email);

    if(isSent) {
      redirectAttributes.addFlashAttribute("email", email);
      redirectAttributes.addFlashAttribute("flow", "forgot-password");
      return "otp-verification";
    }else {
      redirectAttributes.addFlashAttribute("error", "Email not found!");
      return "redirect:/email-verification";
    }
  }
}
