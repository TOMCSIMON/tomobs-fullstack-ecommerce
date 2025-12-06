package com.tomobs.ecommerce.dto;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class  ProductVariantAddDTO {

    @NotBlank(message = "SKU code is required")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "SKU code must contain only uppercase letters, numbers, and hyphens")
    private String skuCode;

    @NotBlank(message = "Variant name is required")
    @Size(max = 255, message = "Variant name cannot exceed 255 characters")
    private String variantName;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price must have maximum 8 digits and 2 decimal places")
    private BigDecimal price;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @Size(max = 50, message = "Size cannot exceed 50 characters")
    private String size;

    @NotBlank(message = "Color is required")
    @Size(max = 50, message = "Color cannot exceed 50 characters")
    private String color;

    private Boolean isActive = true;
}