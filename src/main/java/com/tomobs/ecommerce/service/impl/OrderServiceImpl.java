package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.config.CustomUserDetails;
import com.tomobs.ecommerce.dto.OrderDetailsDTO;
import com.tomobs.ecommerce.dto.OrderListDTO;
import com.tomobs.ecommerce.enums.OrderStatus;
import com.tomobs.ecommerce.exception.CartNotFoundException;
import com.tomobs.ecommerce.model.*;
import com.tomobs.ecommerce.enums.*;
import com.tomobs.ecommerce.repository.*;
import com.tomobs.ecommerce.service.OrderService;
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
public class OrderServiceImpl implements OrderService {

  private final OrdersRepository ordersRepository;
  private final OrderItemsRepository orderItemsRepository;
  private final ProductVariantRepository productVariantRepository;
  private final UserRepository userRepository;
  private final UserAddressRepository userAddressRepository;
  private final CartRepository cartRepository;
  private final CartItemsRepository cartItemsRepository;

  public OrderServiceImpl(
      OrdersRepository ordersRepository,
      OrderItemsRepository orderItemsRepository,
      ProductVariantRepository productVariantRepository,
      UserRepository userRepository,
      UserAddressRepository userAddressRepository,
      CartRepository cartRepository,
      CartItemsRepository cartItemsRepository) {
    this.ordersRepository = ordersRepository;
    this.orderItemsRepository = orderItemsRepository;
    this.productVariantRepository = productVariantRepository;
    this.userRepository = userRepository;
    this.userAddressRepository = userAddressRepository;
    this.cartRepository = cartRepository;
    this.cartItemsRepository = cartItemsRepository;
  }

  // METHOD FOR GET CURRENT USER FROM SESSION
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

  // METHOD FOR PLACE ORDER
  @Override
  @Transactional
  public Long placeOrder(String email, Long addressId, String paymentMethod) {
    // EXTRACTING USER BY EMAIL
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new RuntimeException("email not found"));

    // EXTRACTING ADDRESS BY ADDRESS ID
    Address address =
        userAddressRepository
            .findById(addressId)
            .orElseThrow(() -> new RuntimeException("address not found"));

    // EXTRACTING CART BY USER ID
    Cart cart =
        cartRepository
            .findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("Cart not found"));

    // CHECKING SESSION USER AND DB USER BY ID
    if (!address.getUser().getId().equals(user.getId())) {
      throw new RuntimeException("Address does not belong to user");
    }

    // FETCHING CART ITEMS
    List<CartItems> cartItems = cartItemsRepository.findAllByCart(cart);
    if (cartItems.isEmpty()) {
      throw new CartNotFoundException("Cart is empty");
    }
    // VALIDATING STOCK & CALCULATE TOTAL
    BigDecimal totalAmount = BigDecimal.ZERO;
    for (CartItems items : cartItems) {
      ProductVariant variant = items.getProductVariant();
      if (variant.getStock() < items.getQuantity()) {
        throw new RuntimeException("Insufficient stock for product: " + variant.getId());
      }
      BigDecimal itemTotal = variant.getPrice().multiply(BigDecimal.valueOf(items.getQuantity()));

      totalAmount = totalAmount.add(itemTotal);
    }
    // CREATE ORDERS
    Orders orders = new Orders();
    orders.setUser(user);
    orders.setAddress(address);
    orders.setTotalAmount(totalAmount);

    // CONVERTING STRING TO ENUM
    PaymentType paymentType;
    try {
      paymentType = PaymentType.valueOf(paymentMethod.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Invalid Payment method:" + paymentMethod);
    }
    orders.setPaymentType(paymentType);
    orders.setPaymentStatus(
        paymentType == PaymentType.CASH_ON_DELIVERY
            ? PaymentStatus.PENDING
            : PaymentStatus.INITIATED);
    orders.setStatus(OrderStatus.PLACED);
    Orders savedOrder = ordersRepository.save(orders);

    // LOOP THROUGH CART ITEMS AGAIN TO CREATE ORDER_ITEMS RECORDS
    for (CartItems item : cartItems) {
      ProductVariant variant = item.getProductVariant();
      OrderItems orderItem = new OrderItems();
      orderItem.setOrders(savedOrder);
      orderItem.setProductVariant(variant);
      orderItem.setQuantity(item.getQuantity());
      orderItem.setPriceAtPurchase(item.getPricePerUnit());

      orderItemsRepository.save(orderItem);

      // REDUCING STOCK FROM PRODUCT VARIANT
      variant.setStock(variant.getStock() - item.getQuantity());
      productVariantRepository.save(variant);
    }

    // CLEARING THE CART FOR THE USER
    cartItemsRepository.deleteByCart(cart);
    return savedOrder.getId();
  }

  // METHOD FOR FETCHING ORDER HISTORY
  @Override
  public Page<OrderListDTO> findOrders(int page, int size) {

    User user =
        userRepository
            .findById(getCurrentUserId())
            .orElseThrow(() -> new RuntimeException("User not found!"));

    Pageable pageable = PageRequest.of(page, size);

      return ordersRepository.findByUser(user, pageable);
  }


}
