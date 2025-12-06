package com.tomobs.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "products")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "product_name", nullable = false)
  private String productName;

  //Many products belong to one brand
  @ManyToOne
  @JoinColumn(name = "brand_id", nullable = false)
  private Brand brand;

  // Many products belong to one category
  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

}
