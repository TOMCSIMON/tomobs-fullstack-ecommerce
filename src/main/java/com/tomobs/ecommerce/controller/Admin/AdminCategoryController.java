package com.tomobs.ecommerce.controller.Admin;

import com.tomobs.ecommerce.dto.CategoryDTO;
import com.tomobs.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {

        this.categoryService = categoryService;
    }

    @GetMapping
    public String showNewCategory(){

        return "admin/category";
    }



    @PostMapping("/new")
    public String saveCategory(
           @Valid @ModelAttribute("categoryDTO") CategoryDTO categoryDTO){

        categoryService.addCategory(categoryDTO);

        return "redirect:/admin/categories";
    }
}
