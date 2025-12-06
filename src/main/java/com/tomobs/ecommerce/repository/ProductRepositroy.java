package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepositroy extends JpaRepository<Product, Long> {}
