package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.WishlistVariantDTO;
import com.tomobs.ecommerce.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/wishlist")
@Controller
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping()
    public String showWishlist(
            Principal principal,
            Model model)
    {
        String email = principal.getName();
        List<WishlistVariantDTO> wishlistItems = wishlistService.getWishlist(email);
        model.addAttribute("wishlistItems", wishlistItems);
        return "wishlist";
    }
}
