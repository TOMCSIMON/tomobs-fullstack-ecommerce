package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.OrderListDTO;
import com.tomobs.ecommerce.model.Orders;
import org.springframework.data.domain.Page;

public interface OrderService {

    Long placeOrder(String email,Long addressId,String paymentMethod);

    Page<OrderListDTO> findOrders(int page, int size);

    Page<OrderListDTO> findOrdersWithSearch(int page, int size, String search);

    Orders getOrderById(Long orderId);

    void confirmPayment(Long orderId, String paymentId);

    Long placeOrderForBuyNow(Long variantId,String email,Long addressId,String paymentMethod);

    void saveCancelRequest(String email, Long orderId, String cancelReason);

}