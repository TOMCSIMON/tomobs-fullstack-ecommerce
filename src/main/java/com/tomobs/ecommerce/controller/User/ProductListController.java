package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.UserProductDetailsDTO;
import com.tomobs.ecommerce.dto.UserProductListDTO;
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

@Controller
@RequestMapping("/products")
@Slf4j
public class ProductListController {

    private final UserProductService userProductService;
    private final UserProductDetailService userProductDetailService;

    public ProductListController(UserProductService userProductService,
                                 UserProductDetailService userProductDetailService) {

        this.userProductService = userProductService;
        this.userProductDetailService = userProductDetailService;
    }

    @GetMapping()
    public String showProductListPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            Model model){

        Page<UserProductListDTO> products = userProductService.getProductForListing( page, size);

        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());

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
