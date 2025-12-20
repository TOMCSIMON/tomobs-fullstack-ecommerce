package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.ProductAddDTO;
import com.tomobs.ecommerce.dto.ProductListDTO;
import com.tomobs.ecommerce.model.Brand;
import com.tomobs.ecommerce.model.Category;
import com.tomobs.ecommerce.model.Product;
import com.tomobs.ecommerce.repository.BrandRepository;
import com.tomobs.ecommerce.repository.CategoryRepository;
import com.tomobs.ecommerce.repository.ProductRepository;
import com.tomobs.ecommerce.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              BrandRepository brandRepository,
                              CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Long addProductAndReturnId(ProductAddDTO productAddDTO) {


        Brand brand = brandRepository.findById(productAddDTO.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found!"));

        Category category = categoryRepository.findById(productAddDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found!"));

        Product product = new Product();
        product.setProductName(productAddDTO.getProductName());
        product.setCategory(category);
        product.setBrand(brand);

        productRepository.save(product);

        return product.getId();
    }

    @Override
    public Page<ProductListDTO> getPaginatedProducts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage = productRepository.findAll(pageable);

        return productPage.map(product -> new ProductListDTO(
                product.getId(),
                product.getProductName(),
                product.getCategory().getName(),
                product.getBrand().getName()
        ));

        }
    }



