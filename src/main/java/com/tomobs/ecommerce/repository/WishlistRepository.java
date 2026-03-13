package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.model.User;
import com.tomobs.ecommerce.model.Wishlist;
import com.tomobs.ecommerce.model.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Wishlist findByUser(User user);
}
