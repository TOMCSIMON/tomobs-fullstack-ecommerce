package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.ProductVariantAddDTO;
import com.tomobs.ecommerce.model.Product;
import com.tomobs.ecommerce.model.ProductVariant;
import com.tomobs.ecommerce.repository.ProductRepository;
import com.tomobs.ecommerce.repository.ProductVariantRepository;
import com.tomobs.ecommerce.service.ProductVariantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductVariantServiceImpl implements ProductVariantService {

  private final ProductRepository productRepositroy;
  private final ProductVariantRepository productVariantRepository;

  public ProductVariantServiceImpl(
          ProductRepository productRepositroy, ProductVariantRepository productVariantRepository) {

    this.productRepositroy = productRepositroy;
    this.productVariantRepository = productVariantRepository;
  }

  @Override
  @Transactional
  public ProductVariant addProductVariant(ProductVariantAddDTO productVariantAddDTO) {

    ProductVariant productVariant = new ProductVariant();

    Product product =
        productRepositroy
            .findById(productVariantAddDTO.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found!"));

    // MAPPING TO ENTITY FROM DTO AND SAVING

    productVariant.setSkuCode(productVariantAddDTO.getSkuCode());
    productVariant.setVariantName(productVariantAddDTO.getVariantName());
    productVariant.setRam(productVariantAddDTO.getRam());
    productVariant.setStorage(productVariantAddDTO.getStorage());
    productVariant.setKeyFeatures(productVariantAddDTO.getKeyFeatures());
    productVariant.setPrice(productVariantAddDTO.getPrice());
    productVariant.setStock(productVariantAddDTO.getStock());
    productVariant.setColor(productVariantAddDTO.getColor());
    productVariant.setProduct(product);

    productVariantRepository.save(productVariant);
      return productVariant;
  }
}
