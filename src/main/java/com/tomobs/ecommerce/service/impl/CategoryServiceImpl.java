package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.CategoryDTO;
import com.tomobs.ecommerce.model.Category;
import com.tomobs.ecommerce.repository.CategoryRepository;
import com.tomobs.ecommerce.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

  @Override
  public List<Category> getAllCategories() {

    return categoryRepository.findAll();
  }

  // FOR PAGINATION
  @Override
  public Page<CategoryDTO> getAllCategoriesPaginated(
      int page, int size, String sortField, String sortDirection) {

    Sort sort =
        sortDirection.equalsIgnoreCase("asc")
            ? Sort.by(sortField).ascending()
            : Sort.by(sortField).descending();

    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Category> categoryPage = categoryRepository.findAll(pageable);

    return mapToDTO(categoryPage);
  }

  private Page<CategoryDTO> mapToDTO(Page<Category> categoryPage) {

    return categoryPage.map(
        category -> {
          CategoryDTO dto = new CategoryDTO();

          dto.setId(category.getId());
          dto.setName(category.getName());
          dto.setDescription(category.getDescription());
          dto.setActive(category.isActive());

          return dto;
        });
  }
}
