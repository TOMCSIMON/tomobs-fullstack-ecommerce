package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.config.CustomUserDetails;
import com.tomobs.ecommerce.dto.OrderListDTO;
import com.tomobs.ecommerce.enums.OrderStatus;
import com.tomobs.ecommerce.exception.CartNotFoundException;
import com.tomobs.ecommerce.model.*;
import com.tomobs.ecommerce.enums.*;
import com.tomobs.ecommerce.repository.*;
import com.tomobs.ecommerce.service.CartService;
import com.tomobs.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrdersRepository ordersRepository;
  private final OrderItemsRepository orderItemsRepository;
  private final ProductVariantRepository productVariantRepository;
  private final UserRepository userRepository;
  private final UserAddressRepository userAddressRepository;
  private final CartRepository cartRepository;
  private final CartItemsRepository cartItemsRepository;
  private final CartService cartService;

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null) {
      throw new IllegalStateException("No user details found");
    }
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

    if (userDetails == null) {
      throw new IllegalStateException("No user details found");
    }
    return userDetails.getId();
  }

  @Override
  @Transactional
  public Long placeOrderForBuyNow(Long variantId,String email,Long addressId,String paymentMethod) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("email not found"));

    Address address = userAddressRepository.findById(addressId)
            .orElseThrow(() -> new RuntimeException("address not found"));

    ProductVariant variant = productVariantRepository.findById(variantId)
            .orElseThrow(() -> new RuntimeException("variant not found"));

    Orders orders = new Orders();
    orders.setUser(user);
    orders.setAddress(address);
    orders.setTotalAmount(variant.getPrice());
    PaymentType paymentType;
    try {
      paymentType = PaymentType.valueOf(paymentMethod.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Invalid Payment method:" + paymentMethod);
    }
    orders.setPaymentType(paymentType);
    orders.setPaymentStatus(paymentType == PaymentType.CASH_ON_DELIVERY ? PaymentStatus.PENDING : PaymentStatus.INITIATED);
    orders.setStatus(OrderStatus.PLACED);
    Orders savedOrder = ordersRepository.save(orders);

    OrderItems orderItem = new OrderItems();
    orderItem.setOrders(savedOrder);
    orderItem.setProductVariant(variant);
    orderItem.setQuantity(1);
    orderItem.setPriceAtPurchase(variant.getPrice());

    orderItemsRepository.save(orderItem);

    if (paymentType == PaymentType.CASH_ON_DELIVERY) {
      variant.setStock(variant.getStock() - 1);
      productVariantRepository.save(variant);
    }
    return savedOrder.getId();
  }

  @Override
  @Transactional
  public Long placeOrder(String email, Long addressId, String paymentMethod) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("email not found"));

    Address address = userAddressRepository.findById(addressId)
            .orElseThrow(() -> new RuntimeException("address not found"));

    Cart cart = cartRepository.findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("Cart not found"));

    if (!address.getUser().getId().equals(user.getId())) {
      throw new RuntimeException("Address does not belong to user");
    }

    List<CartItems> cartItems = cartItemsRepository.findAllByCart(cart);
    if (cartItems.isEmpty()) {
      throw new CartNotFoundException("Cart is empty");
    }
    Double finalCartTotal = cartService.calculateTotal(user.getId());
    Orders orders = new Orders();
    orders.setUser(user);
    orders.setAddress(address);
    orders.setTotalAmount(BigDecimal.valueOf(finalCartTotal));

    PaymentType paymentType;
    try {
      paymentType = PaymentType.valueOf(paymentMethod.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Invalid Payment method:" + paymentMethod);
    }
    orders.setPaymentType(paymentType);
    orders.setPaymentStatus(paymentType == PaymentType.CASH_ON_DELIVERY ? PaymentStatus.PENDING : PaymentStatus.INITIATED);
    orders.setStatus(OrderStatus.PLACED);
    Orders savedOrder = ordersRepository.save(orders);

    for (CartItems item : cartItems) {
      ProductVariant variant = item.getProductVariant();
      OrderItems orderItem = new OrderItems();
      orderItem.setOrders(savedOrder);
      orderItem.setProductVariant(variant);
      orderItem.setQuantity(item.getQuantity());
      orderItem.setPriceAtPurchase(item.getPricePerUnit());

      orderItemsRepository.save(orderItem);

      if (paymentType == PaymentType.CASH_ON_DELIVERY) {
        variant.setStock(variant.getStock() - item.getQuantity());
        productVariantRepository.save(variant);
      }
    }
    if (paymentType == PaymentType.CASH_ON_DELIVERY) {
      cartItemsRepository.deleteByCart(cart);
    }
    return savedOrder.getId();
  }

  @Override
  public Page<OrderListDTO> findOrders(int page, int size) {

    User user = userRepository.findById(getCurrentUserId())
            .orElseThrow(() -> new RuntimeException("User not found!"));

    Pageable pageable = PageRequest.of(page, size);
    return ordersRepository.findByUser(user, pageable);
  }
  @Override
  public Page<OrderListDTO> findOrdersWithSearch(int page, int size, String search) {
    User user = userRepository.findById(getCurrentUserId())
            .orElseThrow(() -> new RuntimeException("User not found!"));

    Pageable pageable = PageRequest.of(page, size);

    if (search == null || search.trim().isEmpty()) {
      return ordersRepository.findByUser(user, pageable);
    }

    return ordersRepository.findByUserAndSearch(user, search.trim(), pageable);
  }

  @Override
  public Orders getOrderById(Long orderId) {

    return ordersRepository.findById(orderId)
        .orElseThrow(() -> new RuntimeException("Order not found!"));
  }

  @Override
  @Transactional
  public void confirmPayment(Long orderId, String paymentId) {
    Orders order = ordersRepository.findById(orderId)
          .orElseThrow(() -> new RuntimeException("Order not found"));

    List<OrderItems> orderItems = orderItemsRepository.findByOrders(order);
    for (OrderItems item : orderItems) {
      ProductVariant variant = item.getProductVariant();

      if (variant.getStock() < item.getQuantity()) {

        order.setPaymentStatus(PaymentStatus.FAILED);
        order.setStatus(OrderStatus.CANCELLED);
        ordersRepository.save(order);
        throw new RuntimeException("Sorry, an item in your order went out of stock before payment completed.");
      }
      variant.setStock(variant.getStock() - item.getQuantity());
      productVariantRepository.save(variant);
    }

    order.setPaymentStatus(PaymentStatus.SUCCESS);
    order.setRazorpayPaymentId(paymentId);
    ordersRepository.save(order);

    Cart cart = cartRepository.findByUserId(order.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("Cart not found"));
    cartItemsRepository.deleteByCart(cart);
  }

  @Override
  public Double calculateBuyNowTotal(Long variantId) {

    ProductVariant variant = productVariantRepository.findById(variantId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    return variant.getPrice().doubleValue();
  }

  @Override
  @Transactional
  public void saveCancelRequest(String email, Long orderId, String cancelReason) {

    Orders orders = ordersRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    if (!orders.getUser().getEmail().equals(email)) {
      throw new RuntimeException("You are not authorized to cancel this order");
    }

    if (orders.getStatus() != OrderStatus.PLACED) {
      throw new RuntimeException("Order cannot be cancelled at this stage: " + orders.getStatus());
    }
    orders.setStatus(OrderStatus.PENDING);
    orders.setCancellationReason(cancelReason);
    ordersRepository.save(orders);
  }

  @Override
  @Transactional
  public void saveReturnRequest(String email, Long orderId, String returnReason) {

    Orders orders = ordersRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    if (!orders.getUser().getEmail().equals(email)) {
      throw new RuntimeException("You are not authorized to return this order");
    }

    if (orders.getStatus() != OrderStatus.DELIVERED) {
      throw new RuntimeException("Order cannot be returned at this stage: " + orders.getStatus());
    }
    orders.setStatus(OrderStatus.PENDING);
    orders.setReturnReason(returnReason);
    ordersRepository.save(orders);
  }

  @Override
  public long findOrders() {
    return ordersRepository.count();
  }
}
