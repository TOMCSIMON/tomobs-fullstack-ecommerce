package com.tomobs.ecommerce.dto;

import com.tomobs.ecommerce.enums.OrderStatus;
import com.tomobs.ecommerce.enums.PaymentStatus;
import com.tomobs.ecommerce.enums.PaymentType;
import com.tomobs.ecommerce.model.Address;
import com.tomobs.ecommerce.model.OrderItems;
import com.tomobs.ecommerce.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminOrderDetailsDTO {

    private Long id;
    private List<OrderItems> orderItems;
    private Address address;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private PaymentType paymentType;
    private PaymentStatus paymentStatus;
    private String cancelReason;
    private LocalDateTime createdTime;
    private User user;
}
