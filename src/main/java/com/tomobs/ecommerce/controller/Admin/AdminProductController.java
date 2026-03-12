package com.tomobs.ecommerce.controller.Admin;

import com.tomobs.ecommerce.dto.ProductAddDTO;
import com.tomobs.ecommerce.dto.ProductEditDTO;
import com.tomobs.ecommerce.dto.ProductListDTO;
import com.tomobs.ecommerce.dto.ProductVariantAddDTO;
import com.tomobs.ecommerce.model.ProductVariant;
import com.tomobs.ecommerce.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    @GetMapping
    public String showListProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword,
            Model model) {
        Page<ProductListDTO> productPage = productService.getPaginatedProducts(keyword, page, size);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalElements", productPage.getTotalElements());
        model.addAttribute("pageSize", size);

        return "admin/product";
    }

    @GetMapping("/add")
    public String showAddProductPage(Model model) {

        ProductAddDTO productAddDTO = new ProductAddDTO();

        model.addAttribute("product", productAddDTO);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());

        return "admin/add-product";
    }

    @PostMapping("/add")
    public String addProduct(
            @Valid @ModelAttribute("product") ProductAddDTO productAddDTO,
            BindingResult productResult,
            Model model) throws IOException {

        if (productResult.hasErrors()) {

            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());

            return "admin/add-product";
        }
        productService.addProduct(productAddDTO);

        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductPage(
            @PathVariable("id") Long id,
            Model model) {

        ProductEditDTO productEditDTO = productService.getProductForEdit(id);

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("product", productEditDTO);

        return "admin/edit-product";
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute("product") ProductEditDTO productDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/edit-product";
        }

        try {
            productService.updateProduct(productDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating product: " + e.getMessage());
            return "redirect:/admin/products/edit/" + productDTO.getId();
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(
            @PathVariable("id") Long id) {

        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
