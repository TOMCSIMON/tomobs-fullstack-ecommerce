package com.tomobs.ecommerce.model;

import com.tomobs.ecommerce.enums.OrderStatus;
import com.tomobs.ecommerce.enums.PaymentStatus;
import com.tomobs.ecommerce.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Orders {

  // PRIMARY KEY
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Bidirectional relationship - mapped by 'orders' field in OrderItems entity
  // No additional column created (foreign key exists in order_items table)
  // bidirectional relationship
  @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
  private List<OrderItems> orderItems;

  // FOREIGN KEY
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  // FOREIGN KEY
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id", nullable = false)
  private Address address;

  @Column(name = "total_amount", nullable = false)
  private BigDecimal totalAmount;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private OrderStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_type", nullable = false)
  private PaymentType paymentType;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_status", nullable = false)
  private PaymentStatus paymentStatus;

  @Column(name = "razorpay_order_id")
  private String razorpayOrderId;

  @Column(name = "razorpay_payment_id")
  private String razorpayPaymentId;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

}
