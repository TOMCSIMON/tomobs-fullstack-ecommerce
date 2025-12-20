package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.UserProductDetailsDTO;

public interface UserProductDetailService {

  UserProductDetailsDTO getProductDetailsById(Long productId);

}
