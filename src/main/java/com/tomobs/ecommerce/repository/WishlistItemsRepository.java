package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.model.Wishlist;
import com.tomobs.ecommerce.model.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistItemsRepository extends JpaRepository<WishlistItem, Long> {
    WishlistItem findByWishlist(Wishlist wishlist);
}
