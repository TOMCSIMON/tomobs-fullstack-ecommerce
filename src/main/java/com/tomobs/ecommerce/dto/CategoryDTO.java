package com.tomobs.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {

    @NotBlank(message = "categoryName cannot be empty!")
    @Size(max = 100, message = "Category name cannot exceed 100 characters.")
    private String name;

    @NotBlank(message = "Category description cannot be empty!")
    @Size(max = 200, message = "Category description cannot exceed 200 characters.")
    private String description;
}
