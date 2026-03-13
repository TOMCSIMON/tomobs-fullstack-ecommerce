package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.model.ProductVariant;
import com.tomobs.ecommerce.model.User;
import com.tomobs.ecommerce.model.Wishlist;
import com.tomobs.ecommerce.model.WishlistItem;
import com.tomobs.ecommerce.repository.ProductVariantRepository;
import com.tomobs.ecommerce.repository.UserRepository;
import com.tomobs.ecommerce.repository.WishlistRepository;
import com.tomobs.ecommerce.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class WishlistServiceImpl implements WishlistService {

    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;
    private final ProductVariantRepository productVariantRepository;

    @Override
    public void getWishlist(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        getOrCreateWishlist(user);
    }

    @Override
    @Transactional
    public void addToWishlist(String email, Long variantId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Product Variant not found"));

        Wishlist wishlist = getOrCreateWishlist(user);
        boolean alreadyExists = wishlist.getItems().stream()
                .anyMatch(item -> item.getProductVariant().getId().equals(variantId));

        if (!alreadyExists) {
            WishlistItem newItem = new WishlistItem();
            newItem.setWishlist(wishlist);
            newItem.setProductVariant(variant);

            wishlist.getItems().add(newItem);
            wishlistRepository.save(wishlist);
        }
    }


    private Wishlist getOrCreateWishlist(User user) {
        Wishlist wishlist = wishlistRepository.findByUser(user);
        if (wishlist == null) {
            wishlist = new Wishlist();
            wishlist.setUser(user);
            wishlist = wishlistRepository.save(wishlist);
        }
        return wishlist;
    }

}