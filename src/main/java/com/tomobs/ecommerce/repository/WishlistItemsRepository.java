package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.dto.WishlistVariantDTO;
import com.tomobs.ecommerce.model.ProductVariant;
import com.tomobs.ecommerce.model.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishlistItemsRepository extends JpaRepository<WishlistItem, Long> {
    @Query("""
    SELECT new com.tomobs.ecommerce.dto.WishlistVariantDTO(
        wi.productVariant.id,
        wi.productVariant.product.productName,
        wi.productVariant.variantName,
        wi.productVariant.price,
        vi.fileName
    )
    FROM WishlistItem wi
    LEFT JOIN wi.productVariant.images vi
    WHERE wi.wishlist.id = :wishlistId
    AND (vi.isPrimary = true OR vi IS NULL)
""")
    List<WishlistVariantDTO> findVariantDetailsByWishlistId(@Param("wishlistId") Long wishlistId);

    void deleteByProductVariant(ProductVariant variant);

    boolean existsByProductVariant(ProductVariant variant);
}
