package com.tomobs.ecommerce.dto;

import com.tomobs.ecommerce.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserProductDetailsDTO {

    private Long Id;

    private String productName;

    private List<UserProductVariantDTO> variants;

}
