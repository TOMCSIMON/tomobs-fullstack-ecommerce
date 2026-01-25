package com.tomobs.ecommerce.model;

import jakarta.persistence.Column;

public class LabelValue {


    @Column(name = "RAM", nullable = false)
    private String ram;

    @Column(name = "storage", nullable = false)
    private String storage;

    @Column(name = "color", nullable = false)
    private String color;

}
