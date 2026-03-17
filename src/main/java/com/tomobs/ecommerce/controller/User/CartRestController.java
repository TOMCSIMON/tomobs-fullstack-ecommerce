package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartRestController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Boolean>> addToCart(@RequestParam Long variantId){
        cartService.addToCart(variantId);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/update-quantity")
    public ResponseEntity<Map<String, Boolean>> updateQuantity(
            @RequestParam Long cartItemId,
            @RequestParam int quantity) {

        cartService.updateQuantity(cartItemId, quantity);
        return ResponseEntity.ok(Map.of("success", true));
    }

}
