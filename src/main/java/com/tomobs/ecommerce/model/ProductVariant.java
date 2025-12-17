package com.tomobs.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "product_variants")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SKU_code", nullable = false,unique = true)
    private String skuCode;

    @Column(name = "variant_name", nullable = false)
    private String variantName;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "RAM", nullable = false)
    private String ram;

    @Column(name = "storage", nullable = false)
    private String storage;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "key_features", nullable = false)
    private String keyFeatures;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VariantImage> images = new ArrayList<>();

    public VariantImage getPrimaryImage() {
        return images.stream()
                .filter(VariantImage::isPrimary)
                .findFirst()
                .orElse(null);
    }
}
