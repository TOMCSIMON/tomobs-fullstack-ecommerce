package com.tomobs.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class UserProductVariantDTO {

    private Long id;

    private String name;

    private List<VariantImageDTO> imageUrls;

    private BigDecimal price;

    private String ram;

    private String storage;

    private String color;

    private String keyFeatures;

}
