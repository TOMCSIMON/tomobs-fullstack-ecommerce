package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.WishlistVariantDTO;

import java.util.List;

public interface WishlistService {

    List<WishlistVariantDTO> getWishlist(String email);

    void addToWishlist(String email, Long variantId);
}
