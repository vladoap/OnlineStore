package com.example.MyStore.model.service;

import com.example.MyStore.model.entity.Category;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.entity.User;
import com.example.MyStore.model.enums.CategoryNameEnum;

import java.math.BigDecimal;
import java.util.List;

public class ProductDetailsServiceModel {

    private Long id;
    private String name;
    private String description;
    private User seller;
    private CategoryNameEnum category;
    private List<Picture> pictures;
    private Integer quantity;
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public ProductDetailsServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductDetailsServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductDetailsServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public User getSeller() {
        return seller;
    }

    public ProductDetailsServiceModel setSeller(User seller) {
        this.seller = seller;
        return this;
    }

    public CategoryNameEnum getCategory() {
        return category;
    }

    public ProductDetailsServiceModel setCategory(CategoryNameEnum category) {
        this.category = category;
        return this;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public ProductDetailsServiceModel setPictures(List<Picture> pictures) {
        this.pictures = pictures;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductDetailsServiceModel setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductDetailsServiceModel setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
