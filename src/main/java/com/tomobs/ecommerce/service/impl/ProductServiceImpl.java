package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.ProductAddDTO;
import com.tomobs.ecommerce.model.Product;
import com.tomobs.ecommerce.repository.ProductRepositroy;
import com.tomobs.ecommerce.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepositroy productRepository;

    public ProductServiceImpl(ProductRepositroy productRepository) {

        this.productRepository = productRepository;
    }

    @Override
    public void addProduct(ProductAddDTO productAddDTO) {

        Product product = new Product();

        product.setProductName(productAddDTO.getProductName());
        product.setCategory(productAddDTO.getCategory());
        product.setBrand(productAddDTO.getBrand());

        productRepository.save(product);
    }

}
