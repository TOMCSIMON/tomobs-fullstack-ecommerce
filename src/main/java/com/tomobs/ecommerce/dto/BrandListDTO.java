package com.tomobs.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandListDTO {

    private Long id;

    @NotBlank(message = "Brand Name cannot be empty!")
    @Size(max = 100, message = "Brand name cannot exceed 100 characters.")
    private String name;

    private boolean isActive;

}
