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
    model.addAttribute("flow", "signup");
    return "otp-verification";
  }

  @PostMapping("/verify-otp")
  public String verifyOtp(
          @RequestParam("email") String email,
          @RequestParam("otp") String otp,
          @RequestParam("flow") String flow,
          RedirectAttributes redirectAttributes,
          Model model) {

    if("forgot-password".equals(flow)) {
      if(otpService.verifyOtpForgotPassword(email, otp)) {
        redirectAttributes.addFlashAttribute("email", email);
        return "reset-password";
      } else {
        redirectAttributes.addFlashAttribute("email", email);
        redirectAttributes.addFlashAttribute("otpError", "otp verification failed!");
        return "redirect:/otp-verification";
      }
    }else if(flow.equals("signup")){
      try {
        otpService.verifyOtpAndCreateUser(email, otp);
        return "redirect:/login?verified=true";
      } catch (RuntimeException ex) {
        model.addAttribute("email", email);
        model.addAttribute("otpError", ex.getMessage());
        return "otp-verification";
      }
    }else {
      throw new RuntimeException("Invalid flow flag for verify otp!");
    }
  }

  @PostMapping("/forgot-password/email")
  public String processForgotPassword(
          @RequestParam("email") String email,
          Model model) {

    log.info("FORGOT PASSWORD BACKEND START: {}", email);
    boolean isSent = otpService.generateAndSendOtpForForgotPassword(email);

    if(isSent) {
      model.addAttribute("email", email);
      model.addAttribute("flow", "forgot-password");
      return "otp-verification";
    }else {
      model.addAttribute("error", "Email not found!");
      return "redirect:/email-verification";
    }
  }

}
