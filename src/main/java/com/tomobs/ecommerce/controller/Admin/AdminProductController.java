package com.tomobs.ecommerce.controller.Admin;

import com.tomobs.ecommerce.dto.ProductAddDTO;
import com.tomobs.ecommerce.service.BrandService;
import com.tomobs.ecommerce.service.CategoryService;
import com.tomobs.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;


    public AdminProductController(
            ProductService productService,
            CategoryService categoryService,
            BrandService brandService)
    {
            this.productService = productService;
            this.categoryService = categoryService;
            this.brandService = brandService;
            }

        @GetMapping()
        public String showListProducts() {

            return "admin/product";
        }

        @GetMapping("/new")
        public String addProduct(Model model) {
            model.addAttribute("product", new ProductAddDTO());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            return "admin/add-product";
        }

        @PostMapping("/add")
        public String addProduct(@Valid @ModelAttribute("product") ProductAddDTO productAddDTO,
                                  BindingResult result,
                                  Model model) {
            if (result.hasErrors()) {
                model.addAttribute("categories", categoryService.getAllCategories());
                model.addAttribute("brands", brandService.getAllBrands());
                return "admin/add-product";
            }
            productService.addProduct(productAddDTO);
            return "redirect:/admin/products";
        }
    }

