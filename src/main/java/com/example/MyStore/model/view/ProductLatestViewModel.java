package com.example.MyStore.model.view;

import java.math.BigDecimal;

public class ProductLatestViewModel {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public ProductLatestViewModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductLatestViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductLatestViewModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductLatestViewModel setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductLatestViewModel setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
