package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.ProductVariantAddDTO;
import com.tomobs.ecommerce.model.ProductVariant;

public interface ProductVariantService {

    ProductVariant addProductVariant(ProductVariantAddDTO productVariantAddDTO);

}
