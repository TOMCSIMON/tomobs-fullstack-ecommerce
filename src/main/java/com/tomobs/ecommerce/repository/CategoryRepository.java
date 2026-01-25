package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>{

    Page<Category> findAllByIsDeleted(boolean isDeleted, Pageable pageable);

    Page<Category> findAllByIsDeletedAndNameContainingIgnoreCase(boolean isDeleted,String name, Pageable pageable);
}
