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
import java.io.IOException;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final ProductVariantService productVariantService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final  VariantImageService variantImageService;

    // PAGINATED PRODUCT LIST
    @GetMapping
    public String showListProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        Page<ProductListDTO> productPage = productService.getPaginatedProducts(page, size);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalElements", productPage.getTotalElements());
        model.addAttribute("pageSize", size);

        return "admin/product";
    }

    //LOADING THE ADD PRODUCT PAGE
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

        Long savedProductId = productService.addProductAndReturnId(productAddDTO);

        for(ProductVariantAddDTO v : productAddDTO.getVariants()) {

            v.setProductId(savedProductId);
            ProductVariant savedVariant = productVariantService.addProductVariant(v);

            List<MultipartFile> validImages = v.getImages().stream()
                    .filter(file -> file != null && !file.isEmpty())
                    .toList();

            for(MultipartFile file : validImages) {
                variantImageService.saveImage(file, savedVariant);
            }
        }
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

}
