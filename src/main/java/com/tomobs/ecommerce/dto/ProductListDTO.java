package com.tomobs.ecommerce.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductListDTO {

    private Long productId;

    private String productName;

    private String categoryName;

    private String brandName;

}

