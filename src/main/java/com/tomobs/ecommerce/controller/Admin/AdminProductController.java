package com.tomobs.ecommerce.controller.Admin;

import com.tomobs.ecommerce.dto.ProductAddDTO;
import com.tomobs.ecommerce.dto.ProductListDTO;
import com.tomobs.ecommerce.dto.ProductVariantAddDTO;
import com.tomobs.ecommerce.model.ProductVariant;
import com.tomobs.ecommerce.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;
    private final ProductVariantService productVariantService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final  VariantImageService variantImageService;

    public AdminProductController(
            ProductService productService,
            ProductVariantService productVariantService,
            VariantImageService variantImageService,
            CategoryService categoryService,
            BrandService brandService) {
        this.productService = productService;
        this.productVariantService = productVariantService;
        this.variantImageService = variantImageService;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

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

//        productAddDTO.getVariants().add(new ProductVariantAddDTO());

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
                    .collect(Collectors.toList());



            for(MultipartFile file : validImages) {
                variantImageService.saveImage(file, savedVariant);
            }
        }


        return "redirect:/admin/products";
    }
}
