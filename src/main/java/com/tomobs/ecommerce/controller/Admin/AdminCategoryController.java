package com.tomobs.ecommerce.controller.Admin;

import com.tomobs.ecommerce.dto.CategoryDTO;
import com.tomobs.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

  private final CategoryService categoryService;

  public AdminCategoryController(CategoryService categoryService) {

    this.categoryService = categoryService;
  }

  @GetMapping
  public String showCategories(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(defaultValue = "createdAt") String sortField,
      @RequestParam(defaultValue = "asc") String sortDir,
      Model model) {

    Page<CategoryDTO> categoryPage =
        categoryService.getAllCategoriesPaginated(page, size, sortField, sortDir);

    model.addAttribute("categories", categoryPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPage", categoryPage.getTotalPages());
    model.addAttribute("sortField", sortField);
    model.addAttribute("sortDir", sortDir);
    model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

    return "admin/category";
  }

  @PostMapping("/new")
  public String saveCategory(@Valid @ModelAttribute("categoryDTO") CategoryDTO categoryDTO) {

    categoryService.addCategory(categoryDTO);

    return "redirect:/admin/categories";
  }
}
