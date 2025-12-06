package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.BrandDTO;
import com.tomobs.ecommerce.dto.BrandListDTO;
import com.tomobs.ecommerce.model.Brand;
import com.tomobs.ecommerce.model.Category;
import com.tomobs.ecommerce.repository.BrandRepository;
import com.tomobs.ecommerce.repository.CategoryRepository;
import com.tomobs.ecommerce.service.BrandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

  private final BrandRepository brandRepository;
  private final CategoryRepository categoryRepository;

  public BrandServiceImpl(BrandRepository brandRepository, CategoryRepository categoryRepository) {

    this.brandRepository = brandRepository;
    this.categoryRepository = categoryRepository;
  }

  @Override
  public void addBrand(BrandDTO brandDTO) {

    Category category =
        categoryRepository
            .findById(brandDTO.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found!"));

    Brand brand = new Brand();

    brand.setCategory(category);
    brand.setName(brandDTO.getName());

    brandRepository.save(brand);
  }

  //FOR GET ALL BRANDS
  @Override
  public List<Brand> getAllBrands() {

      return brandRepository.findAll();
  }


    // FOR PAGINATION
    @Override
    public Page<BrandListDTO> getAllBrandsPaginated(
            int page, int size, String sortField, String sortDirection) {

        Sort sort =
                sortDirection.equalsIgnoreCase("asc")
                        ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Brand> brandPage =  brandRepository.findAll(pageable);

        return mapToDTO(brandPage);


    }

    private Page<BrandListDTO> mapToDTO(Page<Brand> brandPage) {

        return brandPage.map(brand -> {
            BrandListDTO dto = new BrandListDTO();

            dto.setId(brand.getId());
            dto.setName(brand.getName());

            return dto;
        });
    }
}
