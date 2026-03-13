package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.UserProductListDTO;
import com.tomobs.ecommerce.model.Product;
import com.tomobs.ecommerce.model.ProductVariant;
import com.tomobs.ecommerce.model.VariantImage;
import com.tomobs.ecommerce.repository.ProductRepository;
import com.tomobs.ecommerce.service.UserProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserProductServiceImpl implements UserProductService {

  private final ProductRepository productRepository;

  @Override
  public Page<UserProductListDTO> getProductForListing(int page, int size) {

    Pageable pageable = PageRequest.of(page, size);
    Page<Product> products = productRepository.findAllByOrderByCreatedAtDesc(pageable);
    return products.map(this::convertToDTO);
  }

  @Override
  public Page<UserProductListDTO>  getFilteredProducts(List<Long> categories, List<Long> brands, List<String> rams, List<String> storages, String sort, int page, int size) {

    Sort JpaSort = Sort.by("createdAt").descending();
    if("PriceAsc".equalsIgnoreCase(sort)){
      JpaSort = Sort.by(Sort.Direction.ASC, "startingPrice");
    }
    if("PriceDesc".equalsIgnoreCase(sort)){
      JpaSort = Sort.by(Sort.Direction.DESC, "startingPrice");
    }
    if("Newest".equalsIgnoreCase(sort)){
      JpaSort = Sort.by(Sort.Direction.DESC, "createdAt");
    }
    Pageable pageable = PageRequest.of(page, size, JpaSort);
    Page<Product> products = productRepository.findFilteredProducts(categories, brands, rams, storages, pageable);
    return products.map(this::convertToDTO);
  }


  private @NonNull UserProductListDTO convertToDTO(Product product) {

    UserProductListDTO dto = new UserProductListDTO();
    dto.setProductId(product.getId());
    dto.setProductName(product.getProductName());

    // FOR FIRST VARIANT FOR A PRODUCT
    if (!product.getVariants().isEmpty()) {
      ProductVariant firstVariant = product.getVariants().get(0);
      dto.setVariantName(firstVariant.getVariantName());
      dto.setPrice(firstVariant.getPrice());

      // FOR PRIMARY IMAGE FOR A VARIANT
      VariantImage primaryImage = firstVariant.getPrimaryImage();
      if(primaryImage != null) {
        String fileName = Paths.get(primaryImage.getFilePath()).getFileName().toString();
        dto.setImageUrl("/uploads/products/" + fileName);
      }
    }
    return dto;
  }

}
