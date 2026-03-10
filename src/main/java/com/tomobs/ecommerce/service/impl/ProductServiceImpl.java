package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.*;
import com.tomobs.ecommerce.model.*;
import com.tomobs.ecommerce.repository.*;
import com.tomobs.ecommerce.service.ProductService;
import com.tomobs.ecommerce.service.VariantImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductVariantRepository productVariantRepository;
  private final VariantImageRepository imageRepository;
  private final BrandRepository brandRepository;
  private final CategoryRepository categoryRepository;
  private final VariantImageService variantImageService;

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
  public Page<ProductListDTO> getPaginatedProducts(String keyword, int page, int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

    Page<Product> productPage;
    if (keyword != null && !keyword.trim().isEmpty()) {
      productPage = productRepository.findByProductNameContainingIgnoreCase(keyword, pageable);
    }else {
      productPage = productRepository.findAll(pageable);
    }

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

  @Override
  @Transactional
  public void updateProduct(ProductEditDTO dto) throws Exception {

    Product existingProduct = productRepository.findById(dto.getId())
            .orElseThrow(() -> new RuntimeException("Product not found"));

    existingProduct.setProductName(dto.getProductName());

    Category category = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found"));
    existingProduct.setCategory(category);

    Brand brand = brandRepository.findById(dto.getBrandId())
            .orElseThrow(() -> new RuntimeException("Brand not found"));
    existingProduct.setBrand(brand);

    for (ProductVariantEditDTO variantDTO : dto.getVariants()) {

      ProductVariant variant;

      if (variantDTO.getId() != null) {
        variant = productVariantRepository.findById(variantDTO.getId())
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        if (variantDTO.getDeletedImageIds() != null && !variantDTO.getDeletedImageIds().isEmpty()) {
          for (Long imageId : variantDTO.getDeletedImageIds()) {
            variantImageService.deleteImage(imageId);
          }
        }
      } else {
        variant = new ProductVariant();
        variant.setProduct(existingProduct);
      }

      variant.setVariantName(variantDTO.getVariantName());
      variant.setSkuCode(variantDTO.getSkuCode());
      variant.setStock(variantDTO.getStock());
      variant.setColor(variantDTO.getColor());
      variant.setPrice(variantDTO.getPrice());
      variant.setRam(variantDTO.getRam());
      variant.setStorage(variantDTO.getStorage());
      variant.setKeyFeatures(variantDTO.getKeyFeatures());

      variant = productVariantRepository.save(variant);

      if (variantDTO.getNewImages() != null && !variantDTO.getNewImages().isEmpty()) {
        for (MultipartFile file : variantDTO.getNewImages()) {
          if (!file.isEmpty()) {
            variantImageService.saveImage(file , variant);
          }
        }
      }
    }

    productRepository.save(existingProduct);
  }

  @Override
  public void deleteProduct(Long id) {

    Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));


    productRepository.deleteById(id);

    List<ProductVariant> variants = productVariantRepository.findByProduct(existingProduct);

    for(ProductVariant variant : variants) {
      productVariantRepository.deleteById(variant.getId());

     List<VariantImage> images = imageRepository.findByProductVariant(variant);

     for(VariantImage image : images) {

       variantImageService.deleteImage(image.getId());
     }
    }

  }
}
