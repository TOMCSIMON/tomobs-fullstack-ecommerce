package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.UserProductListDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserProductService {

    Page<UserProductListDTO> getProductForListing(int page, int size);

    Page<UserProductListDTO>  getFilteredProducts(List<Long> categories, List<Long> brands, List<String> rams, List<String> storages, int page, int size);
}
