package com.tomobs.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
