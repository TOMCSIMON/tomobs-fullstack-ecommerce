package com.tomobs.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private Long id;

    @NotBlank(message = "categoryName cannot be empty!")
    @Size(max = 100, message = "Category name cannot exceed 100 characters.")
    private String name;

    @NotBlank(message = "Category description cannot be empty!")
    @Size(max = 200, message = "Category description cannot exceed 200 characters.")
    private String description;

    private boolean isActive;
}
