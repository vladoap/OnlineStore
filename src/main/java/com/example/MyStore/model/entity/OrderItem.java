package com.example.MyStore.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    private Long id;
    private String name;
    private Integer quantity;
    private BigDecimal price;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public OrderItem setId(Long id) {
        this.id = id;
        return this;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public OrderItem setName(String name) {
        this.name = name;
        return this;
    }

    @Column(nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public OrderItem setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    @Column(nullable = false)
    public BigDecimal getPrice() {
        return price;
    }

    public OrderItem setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
