package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.CategoryDTO;
import com.tomobs.ecommerce.model.Category;
import com.tomobs.ecommerce.repository.CategoryRepository;
import com.tomobs.ecommerce.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public void addCategory(CategoryDTO categoryDTO) {

        Category category = new Category();

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        categoryRepository.save(category);
    }
}
