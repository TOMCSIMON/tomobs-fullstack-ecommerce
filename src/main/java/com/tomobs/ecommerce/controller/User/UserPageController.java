package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.UserRegistrationDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class UserPageController {

  @GetMapping("/")
  public String homePage() {

    return "index";
  }

  @GetMapping("/signup")
  public String signupPage(
          Model model,
          Principal principal)
  {
      if(principal != null){
          return "redirect:/";
      }
      model.addAttribute("user", new UserRegistrationDTO());

    return "signup";
  }

  @GetMapping("/login")
  public String loginPage(Principal principal)
  {

      if(principal != null){
          return "redirect:/";
      }
    return "login";
  }
}
