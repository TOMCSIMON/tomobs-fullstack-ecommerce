package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.model.User;
import com.tomobs.ecommerce.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    boolean existsByUser(User user);
}
