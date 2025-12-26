package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.BrandDTO;
import com.tomobs.ecommerce.dto.CategoryDTO;
import com.tomobs.ecommerce.dto.UserProductDetailsDTO;
import com.tomobs.ecommerce.dto.UserProductListDTO;
import com.tomobs.ecommerce.service.BrandService;
import com.tomobs.ecommerce.service.CategoryService;
import com.tomobs.ecommerce.service.UserProductDetailService;
import com.tomobs.ecommerce.service.UserProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/products")
@Slf4j
public class ProductListController {

    private final UserProductService userProductService;
    private final UserProductDetailService userProductDetailService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    public ProductListController(
            UserProductService userProductService,
            UserProductDetailService userProductDetailService,
            CategoryService categoryService,
            BrandService brandService) {

        this.userProductService = userProductService;
        this.userProductDetailService = userProductDetailService;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    @GetMapping()
    public String showProductListPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            Model model){

        List<CategoryDTO> categories = categoryService.findCategories();
        List<BrandDTO> brands = brandService.findBrands();
        Page<UserProductListDTO> products = userProductService.getProductForListing(categoryId,brandId, page, size);

        log.info("products:{}", products);
        //  Log file

        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("selectedCategoryId", categoryId);

        return "products-list";
    }

    @GetMapping("/detail/{id}")
    public String showProductDetailPage(
            @PathVariable Long id,
            Model model)
    {
        UserProductDetailsDTO productDetail = userProductDetailService.getProductDetailsById(id);

        log.info("Product detail object: {}", productDetail);

        model.addAttribute("productDetail", productDetail);

        return "product-detail";
    }
}
