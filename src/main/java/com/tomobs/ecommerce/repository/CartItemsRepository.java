package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.model.Cart;
import com.tomobs.ecommerce.model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemsRepository extends JpaRepository<CartItems, Long> {

    Optional<CartItems> findByCartIdAndProductVariantId(Long cartId, Long productVariantId);

    List<CartItems> findAllByCart(Cart cart);

    void deleteByCartIdAndProductVariantId(Long cartId, Long productVariantId);

    long countByCartId(Long cartId);
}
