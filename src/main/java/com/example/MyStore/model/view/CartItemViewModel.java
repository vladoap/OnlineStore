package com.example.MyStore.model.view;

import java.math.BigDecimal;

public class CartItemViewModel {

    private Long productId;
    private String name;
    private String imageUrl;
    private Integer quantity;
    private BigDecimal price;

    public Long getProductId() {
        return productId;
    }

    public CartItemViewModel setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public String getName() {
        return name;
    }

    public CartItemViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public CartItemViewModel setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public CartItemViewModel setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public CartItemViewModel setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
