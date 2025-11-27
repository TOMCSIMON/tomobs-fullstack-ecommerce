package com.tomobs.ecommerce.controller;

import com.tomobs.ecommerce.dto.UserRegistrationDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping("/home")
  public String homePage() {

    return "index";
  }

  @GetMapping("/signup")
  public String signupPage(
          Model model)
  {
      model.addAttribute("user", new UserRegistrationDTO());

    return "signup";
  }

  @GetMapping("/login")
  public String loginPage() {

    return "login";
  }
}
