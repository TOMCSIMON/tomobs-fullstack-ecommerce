package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.UserProductListDTO;
import org.springframework.data.domain.Page;

public interface UserProductService {

    Page<UserProductListDTO> getProductForListing(Long categoryId,Long brandId,int page, int size);
}
