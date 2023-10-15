package com.example.MyStore.model.service;

import java.math.BigDecimal;

public class CartItemServiceModel {

    private Long productId;
    private String name;
    private String imageUrl;
    private Integer quantity;
    private BigDecimal price;

    public Long getProductId() {
        return productId;
    }

    public CartItemServiceModel setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public String getName() {
        return name;
    }

    public CartItemServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public CartItemServiceModel setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public CartItemServiceModel setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public CartItemServiceModel setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
