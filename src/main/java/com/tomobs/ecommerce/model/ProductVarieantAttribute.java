package com.tomobs.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "product_variant_attribute")
public class ProductVarieantAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVarient productVarient;

    @Column(name = "attribute_value", nullable = false)
    private String attributeValue;

}
