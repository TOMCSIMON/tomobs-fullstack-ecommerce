package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.ProductAddDTO;
import com.tomobs.ecommerce.dto.ProductListDTO;
import org.springframework.data.domain.Page;

public interface ProductService {

    Long addProductAndReturnId(ProductAddDTO productAddDTO);

    Page<ProductListDTO> getPaginatedProducts(int page, int size);
}
