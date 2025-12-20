package com.tomobs.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantImageDTO {

    private Long id;

    private String imageUrl;

    private boolean isPrimary;
}
