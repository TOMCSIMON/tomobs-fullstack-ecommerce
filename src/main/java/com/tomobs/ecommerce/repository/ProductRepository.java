package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.variants WHERE p.id = :productId")
    Optional<Product> findByIdWithVariantsAndImages(@Param("productId") Long productId);

    Page<Product> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.variants v WHERE " +
            "(:categories IS NULL OR p.category.id IN :categories) AND " +
            "(:brands IS NULL OR p.brand.id IN :brands) AND " +
            "(:rams IS NULL OR v.ram IN :rams) AND " +
            "(:storages IS NULL OR v.storage IN :storages)")
    Page<Product> findFilteredProducts(
            @Param("categories") List<Long> categories,
            @Param("brands") List<Long> brands,
            @Param("rams") List<String> rams,
            @Param("storages") List<String> storages,
            Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.variants v WHERE " +
            "(:search IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.brand.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:categories IS NULL OR p.category.id IN :categories) AND " +
            "(:brands IS NULL OR p.brand.id IN :brands) AND " +
            "(:rams IS NULL OR v.ram IN :rams) AND " +
            "(:storages IS NULL OR v.storage IN :storages)")
    Page<Product> findFilteredProductsWithSearch(
            @Param("search") String search,
            @Param("categories") List<Long> categories,
            @Param("brands") List<Long> brands,
            @Param("rams") List<String> rams,
            @Param("storages") List<String> storages,
            Pageable pageable);
}
