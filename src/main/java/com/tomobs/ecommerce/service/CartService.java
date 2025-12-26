package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.CartDTO;

import java.util.List;

public interface CartService {

    void addToCart(Long variantId);

    List<CartDTO> findCart(Long userId);

    void updateQuantity(Long cartItemId, int quantity);

    Double calculateTotal(Long userId);
}
