package com.tomobs.ecommerce.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class  ProductVariantAddDTO {

    @NotBlank(message = "SKU code is required")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "SKU code must contain only uppercase letters, numbers, and hyphens")
    private String skuCode;

    private Long productId;

    @NotBlank(message = "Variant name is required")
    @Size(max = 255, message = "Variant name cannot exceed 255 characters")
    private String variantName;

    @NotBlank(message = "RAM value required")
    private String ram;

    @NotBlank(message = "Storage value required")
    private String storage;

    @NotBlank(message = "Key features is required")
    private String keyFeatures;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price must have maximum 8 digits and 2 decimal places")
    private BigDecimal price;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @NotBlank(message = "Color is required")
    @Size(max = 50, message = "Color cannot exceed 50 characters")
    private String color;

    private List<MultipartFile> images = new ArrayList<>();

}