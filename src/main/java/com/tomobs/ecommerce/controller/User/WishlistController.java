package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/wishlist")
@Controller
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping()
    public String showWishlist(
            Principal principal)
    {
        String email = principal.getName();
        wishlistService.getWishlist(email);
        return "wishlist";
    }
}
