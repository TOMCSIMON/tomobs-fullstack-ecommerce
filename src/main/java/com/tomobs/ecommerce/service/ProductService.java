package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.ProductAddDTO;
import org.jspecify.annotations.Nullable;

public interface ProductService {

    void addProduct(ProductAddDTO productAddDTO);
}
