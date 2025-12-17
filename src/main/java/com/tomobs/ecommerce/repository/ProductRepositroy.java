package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepositroy extends JpaRepository<Product, Long> {

    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
