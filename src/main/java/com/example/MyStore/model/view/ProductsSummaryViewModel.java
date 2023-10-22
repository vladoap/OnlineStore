package com.example.MyStore.model.view;

import com.example.MyStore.model.entity.Category;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.entity.User;

import java.math.BigDecimal;
import java.util.Set;

public class ProductsSummaryViewModel {


    private Long id;
    private String name;
    private String imageUrl;
    private Integer quantity;
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public ProductsSummaryViewModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductsSummaryViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductsSummaryViewModel setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductsSummaryViewModel setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductsSummaryViewModel setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }
}
