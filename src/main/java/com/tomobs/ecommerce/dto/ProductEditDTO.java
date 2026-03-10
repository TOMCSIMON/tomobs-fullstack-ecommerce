package com.tomobs.ecommerce.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductEditDTO {

    private Long id;
    private String productName;
    private Long brandId;
    private Long categoryId;
    private List<ProductVariantEditDTO> variants = new ArrayList<>();
}
