package com.tomobs.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductAddDTO {

    // Product fields
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 255, message = "Product name must be between 3 and 255 characters")
    private String productName;


    @NotNull(message = "Brand is required")
    private Long brandId;

    @NotNull(message = "Category is required")
    private Long categoryId;

    private List<ProductVariantAddDTO> variants = new ArrayList<>();

}