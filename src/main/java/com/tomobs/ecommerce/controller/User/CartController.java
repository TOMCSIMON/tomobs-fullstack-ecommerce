package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.CartDTO;
import com.tomobs.ecommerce.model.User;
import com.tomobs.ecommerce.service.CartService;
import com.tomobs.ecommerce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping()
    public String viewCart(
            Model model,
            Principal principal)
    {
        String email= principal.getName();
        User user = userService.findByEmail(email);
        List<CartDTO> cartItems = cartService.findCart(user.getId());
        Double totalAmount = cartService.calculateTotal(user.getId());

        log.info("cartDetails:{}" , cartItems);
        log.info("totalPrice:{}" , totalAmount);


        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalAmount);
        return "cart";
    }


    @PostMapping("/add")
    public String addToCart(@RequestParam Long variantId){

        cartService.addToCart(variantId);

        return "redirect:/cart";
    }

    @PostMapping("/update-quantity")
    @ResponseBody
    public String updateQuantity(
            @RequestParam Long cartItemId,
            @RequestParam int quantity) {

        cartService.updateQuantity(cartItemId, quantity);
        return "OK";
    }

}
