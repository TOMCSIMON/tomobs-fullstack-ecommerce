package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.AdminOrderDetailsDTO;
import com.tomobs.ecommerce.dto.OrderDetailsDTO;
import com.tomobs.ecommerce.enums.OrderStatus;
import com.tomobs.ecommerce.model.Orders;
import com.tomobs.ecommerce.model.User;
import com.tomobs.ecommerce.repository.OrdersRepository;
import com.tomobs.ecommerce.repository.UserRepository;
import com.tomobs.ecommerce.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderDetailsServiceImpl implements OrderDetailService {

  private final UserRepository userRepository;
  private final OrdersRepository ordersRepository;

  @Override
  @Transactional
  public OrderDetailsDTO findOrderDetails(String email, Long orderId) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found!"));

    Orders orders = ordersRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found!"));

    OrderDetailsDTO detailsDTO = new OrderDetailsDTO();
    detailsDTO.setId(orders.getId());
    detailsDTO.setOrderItems(orders.getOrderItems());
    detailsDTO.setAddress(orders.getAddress());
    detailsDTO.setTotalAmount(orders.getTotalAmount());
    detailsDTO.setStatus(orders.getStatus());
    detailsDTO.setPaymentType(orders.getPaymentType());
    detailsDTO.setCancelReason(orders.getCancellationReason());
    detailsDTO.setReturnReason(orders.getReturnReason());
    detailsDTO.setPaymentStatus(orders.getPaymentStatus());
    detailsDTO.setCreatedTime(orders.getCreatedAt());
    detailsDTO.setUpdatedTime(orders.getUpdatedAt());
    return detailsDTO;
  }

  @Override
  public AdminOrderDetailsDTO findOrderDetailsForAdmin(Long orderId) {

    Orders orders = ordersRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found!"));

    AdminOrderDetailsDTO detailsDTO = new AdminOrderDetailsDTO();
    detailsDTO.setId(orders.getId());
    detailsDTO.setOrderItems(orders.getOrderItems());
    detailsDTO.setAddress(orders.getAddress());
    detailsDTO.setTotalAmount(orders.getTotalAmount());
    detailsDTO.setStatus(orders.getStatus());
    detailsDTO.setPaymentType(orders.getPaymentType());
    detailsDTO.setCancelReason(orders.getCancellationReason());
    detailsDTO.setReturnReason(orders.getReturnReason());
    detailsDTO.setPaymentStatus(orders.getPaymentStatus());
    detailsDTO.setCreatedTime(orders.getCreatedAt());
    detailsDTO.setUser(orders.getUser());
    return detailsDTO;
  }

  @Override
  @Transactional
  public void updateStatus(Long id, OrderStatus status) {

    Orders orders = ordersRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found!"));

    orders.setStatus(status);
    ordersRepository.save(orders);
  }
}
