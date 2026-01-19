package com.tomobs.ecommerce.dto;

import com.tomobs.ecommerce.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OrderListDTO {

    private Long id;

    private String variantName;

    private Long totalItems;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private String imageUrl;

    public OrderListDTO(Long id, String variantName, Long quantity,
                        BigDecimal totalAmount, OrderStatus status, String fileName) {
        this.id = id;
        this.variantName = variantName;
        this.totalItems = quantity;
        this.totalAmount = totalAmount;
        this.status = status;
        this.imageUrl = ("/uploads/products/" + fileName);
    }
}
