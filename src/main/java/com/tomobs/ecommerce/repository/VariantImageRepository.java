package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.model.ProductVariant;
import com.tomobs.ecommerce.model.VariantImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VariantImageRepository extends JpaRepository<VariantImage, Long> {

    long countByProductVariant(ProductVariant variant);

    Optional<VariantImage> findByProductVariantIdAndIsPrimaryTrue(Long variantId);

    List<VariantImage> findByProductVariantId(Long variantId);

}
