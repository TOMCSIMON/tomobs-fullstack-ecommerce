package com.tomobs.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductAddDTO {

    // Product fields
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 255, message = "Product name must be between 3 and 255 characters")
    private String productName;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotNull(message = "Brand is required")
    private Long brandId;

    @NotNull(message = "Category is required")
    private Long categoryId;


    private Boolean isActive = true;
}