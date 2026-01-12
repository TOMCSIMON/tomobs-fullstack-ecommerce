package com.tomobs.ecommerce.service;

public interface OrderService {
    Long placeOrder(String email,Long addressId,String paymentMethod);
}
