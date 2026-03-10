package com.tomobs.ecommerce.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductVariantEditDTO {

    private Long id;
    private String variantName;
    private String skuCode;
    private Integer stock;
    private String color;
    private BigDecimal price;
    private String ram;
    private String storage;
    private String keyFeatures;

    private List<MultipartFile> newImages = new ArrayList<>();
    private List<Long> deletedImageIds = new ArrayList<>();
    private List<VariantImageDTO> existingImages = new ArrayList<>();
}
