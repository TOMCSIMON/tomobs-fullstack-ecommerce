package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/wishlist")
@RestController
public class WishlistRestController {

    private final WishlistService wishlistService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Boolean>> addToWishlist(
            @RequestParam Long variantId,
            Principal principal) {

        String email = principal.getName();
        wishlistService.addToWishlist(email, variantId);
        return ResponseEntity.ok(Map.of("success", true));
    }
}
