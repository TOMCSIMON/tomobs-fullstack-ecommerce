package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.OrderDetailsDTO;
import com.tomobs.ecommerce.dto.OrderListDTO;
import org.springframework.data.domain.Page;

import java.util.List;


public interface OrderService {
    Long placeOrder(String email,Long addressId,String paymentMethod);

    Page<OrderListDTO> findOrders(int page, int size);
}
