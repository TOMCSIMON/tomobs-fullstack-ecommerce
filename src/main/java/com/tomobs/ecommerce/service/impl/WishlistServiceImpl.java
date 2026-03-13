package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.model.User;
import com.tomobs.ecommerce.model.Wishlist;
import com.tomobs.ecommerce.repository.UserRepository;
import com.tomobs.ecommerce.repository.WishlistRepository;
import com.tomobs.ecommerce.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class WishlistServiceImpl implements WishlistService {

    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;

    @Override
    public void getWishlist(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new  RuntimeException("User not found"));

        if(!wishlistRepository.existsByUser(user)) {
            Wishlist wishlist = new Wishlist();
            wishlist.setUser(user);
            wishlistRepository.save(wishlist);
        }
    }
}
