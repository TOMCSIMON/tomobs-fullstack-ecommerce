package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.CartDTO;
import com.tomobs.ecommerce.model.User;
import com.tomobs.ecommerce.service.CartService;
import com.tomobs.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @GetMapping()
    public String viewCart(
            Model model,
            Principal principal)
    {
        String email= principal.getName();
        User user = userService.findByEmail(email);
        List<CartDTO> cartItems = cartService.findCart(user.getId());
        Double totalAmount = cartService.calculateTotal(user.getId());

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalAmount);
        return "cart";
    }
}
