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
public class CartDTO {

    private Long cartItemId;
    private String variantName;
    private BigDecimal price;

    private Integer quantity;
    private BigDecimal pricePerUnit;

    private BigDecimal subtotal;

    private String imageUrl;
}
