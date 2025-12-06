package com.tomobs.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandDTO {

    private Long id;

    @NotBlank(message = "Brand Name cannot be empty!")
    @Size(max = 100, message = "Brand name cannot exceed 100 characters.")
    private String name;


    @NotNull(message = "Category is required")
    private Long categoryId;
}
