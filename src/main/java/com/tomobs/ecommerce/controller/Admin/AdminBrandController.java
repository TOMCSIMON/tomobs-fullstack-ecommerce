package com.tomobs.ecommerce.controller.Admin;

import com.tomobs.ecommerce.dto.BrandDTO;
import com.tomobs.ecommerce.dto.BrandListDTO;
import com.tomobs.ecommerce.dto.CategoryDTO;
import com.tomobs.ecommerce.service.BrandService;
import com.tomobs.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/brands")
@Slf4j
public class AdminBrandController {

    private final BrandService brandService;
    private final CategoryService categoryService;

    public AdminBrandController(BrandService brandService,
                                CategoryService  categoryService) {

        this.brandService = brandService;
        this.categoryService = categoryService;
    }


    @GetMapping
    public String showBrands(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortField,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model) {

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brandDTO", new BrandDTO());

        Page<BrandListDTO> brandPage =
                brandService.getAllBrandsPaginated(page, size, sortField, sortDir);


        model.addAttribute("brands", brandPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "admin/brand";
    }



    @PostMapping("/new")
    public String addBrand(
            @Valid @ModelAttribute("brandDTO")BrandDTO brandDTO,
            BindingResult bindingResult
            ) {

        log.info("brand details:{}", brandDTO);

        if(bindingResult.hasErrors()) {

            return "admin/brand";
        }
        brandService.addBrand(brandDTO);

        return "redirect:/admin/brands";
    }
}
