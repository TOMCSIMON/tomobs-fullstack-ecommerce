package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Boolean>> deleteWishlistItem(
            @RequestParam Long variantId,
            Principal principal) {

        String email = principal.getName();
        wishlistService.deleteWishlist(email, variantId);
        return ResponseEntity.ok(Map.of("success", true));
    }
}
