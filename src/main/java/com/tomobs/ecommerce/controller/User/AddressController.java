package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.UserAddressAddDTO;
import com.tomobs.ecommerce.service.UserAddressService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// FOR HANDLING USER ADDRESS
@Controller
@RequestMapping("/address")
@Slf4j
public class AddressController {

    private final UserAddressService userAddressService;

    public AddressController(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }


    @GetMapping()
    public String viewAddress(Model model){

        model.addAttribute("userAddressAddDTO", new UserAddressAddDTO());
        return "user-address";
    }

    // FOR SAVING USER ADDRESS
    @PostMapping("/add")
    public String addAddress(
            @ModelAttribute("userAddressAddDTO") @Valid UserAddressAddDTO userAddressAddDTO,
            BindingResult bindingResult
    ){

        if (bindingResult.hasErrors()) {
            return "user-address";
        }

        userAddressService.addAddress(userAddressAddDTO);
        return "redirect:/user-address";
    }
}
