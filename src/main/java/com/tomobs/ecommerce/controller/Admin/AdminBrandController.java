package com.tomobs.ecommerce.controller.Admin;

import com.tomobs.ecommerce.dto.BrandDTO;
import com.tomobs.ecommerce.dto.BrandListDTO;
import com.tomobs.ecommerce.service.BrandService;
import com.tomobs.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/brands")
@Slf4j
@RequiredArgsConstructor
public class AdminBrandController {

    private final BrandService brandService;
    private final CategoryService categoryService;

    @GetMapping
    public String showBrands(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortField,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
            Model model) {

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brandDTO", new BrandDTO());

        Page<BrandListDTO> brandPage =
                brandService.getAllBrandsPaginated(page, size, sortField, sortDir, keyword);

        model.addAttribute("brands", brandPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", brandPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);

        if ("XMLHttpRequest".equals(requestedWith)) {
            return "admin/brand :: brandTableFragment";
        }

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

    // FETCH DETAIL FOR EDIT METHOD
    @GetMapping("/getBrand/{id}")
    @ResponseBody
    public BrandDTO viewBrandForEdit(@PathVariable Long id) {
        return brandService.getBrandForEdit(id);
    }

    // UPDATE BRAND
    @PostMapping("/edit/{id}")
    public String updateBrand(
            @PathVariable Long id,
            @ModelAttribute BrandDTO brandDTO
    ){
        brandService.updateBrand(id, brandDTO);
        return "redirect:/admin/brands";
    }

    // DELETE BRAND
    @PostMapping("/delete/{id}")
    @ResponseBody
    public String deleteBrand(@PathVariable Long id) {

        brandService.deleteBrand(id);
        return "redirect:/admin/brands";
    }
}
