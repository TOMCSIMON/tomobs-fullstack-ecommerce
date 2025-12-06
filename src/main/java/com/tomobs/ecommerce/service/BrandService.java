package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.BrandDTO;
import com.tomobs.ecommerce.dto.BrandListDTO;
import com.tomobs.ecommerce.model.Brand;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BrandService {

  void addBrand(BrandDTO brandDTO);

  Page<BrandListDTO> getAllBrandsPaginated(int page, int size, String sortField, String sortDirection);


    List<Brand> getAllBrands();
}
