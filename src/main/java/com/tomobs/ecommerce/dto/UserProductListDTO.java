package com.tomobs.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProductListDTO {

    private Long productId;
    private String productName;

    private Long variantId;
    private String variantName;

    private BigDecimal price;
    private String imageUrl;
}