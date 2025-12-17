package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.UserProductListDTO;
import com.tomobs.ecommerce.model.Product;
import com.tomobs.ecommerce.model.ProductVariant;
import com.tomobs.ecommerce.model.VariantImage;
import com.tomobs.ecommerce.repository.ProductRepositroy;
import com.tomobs.ecommerce.service.UserProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Paths;

@Service
@Transactional(readOnly = true)
public class UserProductServiceImpl implements UserProductService {

  private final ProductRepositroy productRepositroy;

  public UserProductServiceImpl(ProductRepositroy productRepositroy) {

    this.productRepositroy = productRepositroy;
  }

  public Page<UserProductListDTO> getProductForListing(int page, int size) {

    Pageable pageable = PageRequest.of(page, size);

    Page<Product> products = productRepositroy.findAllByOrderByCreatedAtDesc(pageable);

    return products.map(this::convertToDTO);
  }

  // MAPPING METHOD ENTITY TO DTO
  private UserProductListDTO convertToDTO(Product product) {

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
