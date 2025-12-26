package com.tomobs.ecommerce.controller.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/address")
@Slf4j
public class AddressController {

    @GetMapping()
    public String viewAddress(){

        return "user-address";
    }
}
