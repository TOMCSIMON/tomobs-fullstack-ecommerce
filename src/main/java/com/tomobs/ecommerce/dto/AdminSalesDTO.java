package com.tomobs.ecommerce.dto;

import com.tomobs.ecommerce.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminSalesDTO {

    private Long id;
    private String userName;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
}