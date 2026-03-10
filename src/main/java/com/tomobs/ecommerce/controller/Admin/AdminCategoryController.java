package com.tomobs.ecommerce.controller.Admin;

import com.tomobs.ecommerce.dto.CategoryDTO;
import com.tomobs.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

  private final CategoryService categoryService;

  @GetMapping
  public String showCategories(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(defaultValue = "createdAt") String sortField,
      @RequestParam(defaultValue = "desc") String sortDir,
      @RequestParam(value = "keyword", required = false) String keyword,
      Model model) {
    Page<CategoryDTO> categoryPage =
        categoryService.getAllCategoriesPaginated(page, size, sortField, sortDir, keyword);

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

  // DELETE CATEGORY
  @PostMapping("/delete/{id}")
  @ResponseBody
  public String deleteCategory(@PathVariable Long id) {
    categoryService.deleteCategory(id);
    return "redirect:/admin/categories";
  }

  // GET CATEGORY FOR EDIT
  @GetMapping("/getCategory/{id}")
  @ResponseBody
  public CategoryDTO viewCategoryForEdit(@PathVariable Long id) {

    return categoryService.getCategoryForEdit(id);
  }

  // UPDATE CATEGORY
  @PostMapping("/edit/{id}")
  public String updateCategory(
          @PathVariable Long id,
          @ModelAttribute CategoryDTO categoryDTO
  ) {
    log.info("catgorydto: {}", categoryDTO);
    categoryService.updateCategory(id, categoryDTO);
    return "redirect:/admin/categories";
  }
}
