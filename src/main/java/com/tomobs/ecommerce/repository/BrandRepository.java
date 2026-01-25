package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    Page<Brand> findAllByIsDeleted(boolean isDeleted, Pageable pageable);
}
