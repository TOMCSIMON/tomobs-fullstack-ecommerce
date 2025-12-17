package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.UserProductListDTO;
import com.tomobs.ecommerce.service.UserProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/products")
public class ProductListController {

    private final UserProductService userProductService;

    public ProductListController(UserProductService userProductService) {

        this.userProductService = userProductService;
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


}
