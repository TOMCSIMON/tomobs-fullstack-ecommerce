package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.UserProductDetailsDTO;
import com.tomobs.ecommerce.dto.UserProductVariantDTO;
import com.tomobs.ecommerce.dto.VariantImageDTO;
import com.tomobs.ecommerce.model.Product;
import com.tomobs.ecommerce.model.ProductVariant;
import com.tomobs.ecommerce.model.VariantImage;
import com.tomobs.ecommerce.repository.ProductRepository;
import com.tomobs.ecommerce.repository.ProductVariantRepository;
import com.tomobs.ecommerce.repository.VariantImageRepository;
import com.tomobs.ecommerce.service.UserProductDetailService;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserProductDetailServiceImpl implements UserProductDetailService {

  private final ProductRepository productRepository;

  public UserProductDetailServiceImpl(
      ProductRepository productRepository)
  {
    this.productRepository = productRepository;
  }

  public UserProductDetailsDTO getProductDetailsById(Long productId) {
    Product product =
        productRepository
            .findByIdWithVariantsAndImages(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

    product.getVariants().forEach(variant -> variant.getImages().size());
    return convertToDetailDTO(product);
  }

  private UserProductDetailsDTO convertToDetailDTO(Product product) {
    UserProductDetailsDTO dto = new UserProductDetailsDTO();
    dto.setId(product.getId());
    dto.setProductName(product.getProductName());

    List<UserProductVariantDTO> variantDTOs =
        product.getVariants().stream().map(this::convertToVariantDTO).collect(Collectors.toList());
    dto.setVariants(variantDTOs);

    return dto;
  }

  private UserProductVariantDTO convertToVariantDTO(ProductVariant variant) {
    UserProductVariantDTO dto = new UserProductVariantDTO();
    dto.setId(variant.getId());
    dto.setName(variant.getVariantName());
    dto.setKeyFeatures(variant.getKeyFeatures());
    dto.setColor(variant.getColor());
    dto.setRam(variant.getRam());
    dto.setStorage(variant.getStorage());
    dto.setPrice(variant.getPrice());

    // Convert images
    List<VariantImageDTO> imageDTOs =
        variant.getImages().stream().map(this::convertToImageDTO).collect(Collectors.toList());
    dto.setImageUrls(imageDTOs);

    return dto;
  }

  private VariantImageDTO convertToImageDTO(VariantImage image) {
    VariantImageDTO dto = new VariantImageDTO();
    dto.setId(image.getId());
    String fileName = Paths.get(image.getFilePath()).getFileName().toString();
    dto.setImageUrl("/uploads/products/" + fileName);
    dto.setPrimary(image.isPrimary());
    return dto;
  }
}
