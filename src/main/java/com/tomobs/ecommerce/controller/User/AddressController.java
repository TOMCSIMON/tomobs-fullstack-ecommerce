package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.UserAddressAddDTO;
import com.tomobs.ecommerce.dto.UserAddressListDTO;
import com.tomobs.ecommerce.dto.UserProductListDTO;
import com.tomobs.ecommerce.service.UserAddressService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// FOR HANDLING USER ADDRESS
@Controller
@RequestMapping("/address")
@Slf4j
public class AddressController {

    private final UserAddressService userAddressService;

    public AddressController(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }

    // FOR LOADING LOGGED USER ADDRESSES
    @GetMapping()
    public String viewAddress(Model model){

        List<UserAddressListDTO> addressList = userAddressService.getAddress();

        model.addAttribute("userAddressAddDTO", new UserAddressAddDTO());
        model.addAttribute("addressList", addressList);
        return "user-address";
    }

    // FOR SAVING LOGGED USER ADDRESS
    @PostMapping("/add")
    public String addAddress(
            @ModelAttribute("userAddressAddDTO") @Valid UserAddressAddDTO userAddressAddDTO,
            BindingResult bindingResult
    ){

        if (bindingResult.hasErrors()) {
            return "user-address";
        }

        userAddressService.addAddress(userAddressAddDTO);
        return "redirect:/address";
    }

    // FOR DELETING ADDRESS
    @GetMapping("/delete/{id}")
    public String deleteAddress(@PathVariable Long id) {

        userAddressService.deleteAddress(id);
        return "redirect:/address";
    }
}
