package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.model.ProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    @EntityGraph(attributePaths = {"product"})
    Page<ProductVariant> findAll(Pageable pageable);

    List<ProductVariant> findByProductId(Long  id);

}
