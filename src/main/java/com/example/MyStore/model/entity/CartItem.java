package com.example.MyStore.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "cart_items")
public class CartItem extends BaseEntity {

    private Long productId;
    private String name;
    private String imageUrl;
    private Integer quantity;
    private BigDecimal price;

    @Column(nullable = false, name = "product_id")
    public Long getProductId() {
        return productId;
    }

    public CartItem setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public CartItem setName(String name) {
        this.name = name;
        return this;
    }

    @Column(name = "image")
    public String getImageUrl() {
        return imageUrl;
    }

    public CartItem setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    @Column(nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public CartItem setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    @Column(nullable = false)
    public BigDecimal getPrice() {
        return price;
    }

    public CartItem setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
