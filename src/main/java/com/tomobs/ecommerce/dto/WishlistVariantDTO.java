package com.tomobs.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class WishlistVariantDTO {

    Long variantId;
    String productName;
    String variantName;
    BigDecimal price;
    String primaryImageName;
}
