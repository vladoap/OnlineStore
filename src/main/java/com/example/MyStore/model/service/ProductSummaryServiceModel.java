package com.example.MyStore.model.service;

import java.math.BigDecimal;

public class ProductSummaryServiceModel {

    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private String imageUrl;
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public ProductSummaryServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductSummaryServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductSummaryServiceModel setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductSummaryServiceModel setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductSummaryServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductSummaryServiceModel setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }
}
