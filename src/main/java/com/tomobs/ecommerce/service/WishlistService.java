package com.tomobs.ecommerce.service;

public interface WishlistService {

    void getWishlist(String email);

    void addToWishlist(String email, Long variantId);
}
