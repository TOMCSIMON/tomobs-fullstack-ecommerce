package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.AdminOrderDetailsDTO;
import com.tomobs.ecommerce.dto.OrderDetailsDTO;
import com.tomobs.ecommerce.enums.OrderStatus;

public interface OrderDetailService {

    OrderDetailsDTO findOrderDetails(String email, Long orderId);

    AdminOrderDetailsDTO findOrderDetailsForAdmin(Long orderId);

    void updateStatus(Long id, OrderStatus status);
}
