package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.ProductAddDTO;
import com.tomobs.ecommerce.dto.ProductEditDTO;
import com.tomobs.ecommerce.dto.ProductListDTO;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface ProductService {

    void addProduct(ProductAddDTO productAddDTO) throws IOException;

    Page<ProductListDTO> getPaginatedProducts(String keyword, int page, int size);

    ProductEditDTO getProductForEdit(Long id);

    void updateProduct(ProductEditDTO productDTO) throws Exception;

    void deleteProduct(Long id);
}
