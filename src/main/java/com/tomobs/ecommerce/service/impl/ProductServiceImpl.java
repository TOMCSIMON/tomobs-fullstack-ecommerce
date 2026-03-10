package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.*;
import com.tomobs.ecommerce.model.*;
import com.tomobs.ecommerce.repository.*;
import com.tomobs.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductVariantRepository productVariantRepository;
  private final VariantImageRepository imageRepository;
  private final BrandRepository brandRepository;
  private final CategoryRepository categoryRepository;

  @Override
  @Transactional
  public Long addProductAndReturnId(ProductAddDTO productAddDTO) {

    Brand brand =
        brandRepository
            .findById(productAddDTO.getBrandId())
            .orElseThrow(() -> new RuntimeException("Brand not found!"));

    Category category =
        categoryRepository
            .findById(productAddDTO.getCategoryId())
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

    return productPage.map(
        product ->
            new ProductListDTO(
                product.getId(),
                product.getProductName(),
                product.getCategory().getName(),
                product.getBrand().getName()));
  }

  @Override
  public ProductEditDTO getProductForEdit(Long id) {

    Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found!"));

    ProductEditDTO dto = new ProductEditDTO();
    dto.setId(product.getId());
    dto.setProductName(product.getProductName());
    dto.setCategoryId(product.getCategory().getId());
    dto.setBrandId(product.getBrand().getId());

    List<ProductVariant> variants = productVariantRepository.findByProduct(product);

    for(ProductVariant variant : variants) {

      ProductVariantEditDTO variantEditDTO = new ProductVariantEditDTO();
      variantEditDTO.setId(variant.getId());
      variantEditDTO.setVariantName(variant.getVariantName());
      variantEditDTO.setSkuCode(variant.getSkuCode());
      variantEditDTO.setStock(variant.getStock());
      variantEditDTO.setColor(variant.getColor());
      variantEditDTO.setPrice(variant.getPrice());
      variantEditDTO.setRam(variant.getRam());
      variantEditDTO.setStorage(variant.getStorage());
      variantEditDTO.setKeyFeatures(variant.getKeyFeatures());

      List<VariantImage> variantImages = imageRepository.findByProductVariant(variant);

      for(VariantImage images : variantImages) {

        VariantImageDTO imageDTO = new VariantImageDTO();

        imageDTO.setId(images.getId());
        imageDTO.setImageUrl("/uploads/products/" + images.getFileName());
        imageDTO.setPrimary(images.isPrimary());

        variantEditDTO.getExistingImages().add(imageDTO);
      }

      dto.getVariants().add(variantEditDTO);
    }
    return dto;
  }
}
