package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.CategoryDTO;
import com.tomobs.ecommerce.model.Category;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {

  void addCategory(CategoryDTO categoryDTO);

  List<CategoryDTO> findCategories();

  List<Category> getAllCategories();

  Page<CategoryDTO> getAllCategoriesPaginated(int page, int size, String sortField, String sortDirection);
}
