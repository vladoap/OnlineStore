package com.example.MyStore.model.service;

import com.example.MyStore.model.entity.Category;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.entity.User;

import java.math.BigDecimal;
import java.util.List;

public class ProductAddServiceModel {

    private Long id;
    private String name;
    private String description;
    private Category category;
    private List<Picture> pictures;
    private Integer quantity;
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public ProductAddServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductAddServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductAddServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }



    public Category getCategory() {
        return category;
    }

    public ProductAddServiceModel setCategory(Category category) {
        this.category = category;
        return this;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public ProductAddServiceModel setPictures(List<Picture> pictures) {
        this.pictures = pictures;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductAddServiceModel setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductAddServiceModel setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
