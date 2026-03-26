package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.AdminOrderDetailsDTO;
import com.tomobs.ecommerce.dto.OrderDetailsDTO;

public interface OrderDetailService {

    OrderDetailsDTO findOrderDetails(String email, Long orderId);

    AdminOrderDetailsDTO findOrderDetailsForAdmin(Long orderId);
}
