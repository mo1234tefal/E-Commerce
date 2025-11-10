package com.mo.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String name;
    @Column(nullable = false)
    private String  description;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Integer stock;
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;




}
