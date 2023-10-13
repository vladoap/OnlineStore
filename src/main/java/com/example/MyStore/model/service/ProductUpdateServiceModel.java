package com.example.MyStore.model.service;

import com.example.MyStore.model.entity.Category;
import com.example.MyStore.model.entity.Picture;

import java.math.BigDecimal;
import java.util.List;

public class ProductUpdateServiceModel {

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

    public ProductUpdateServiceModel setId(Long id) {
        this.id = id;
        return this;
    }


    public String getName() {
        return name;
    }

    public ProductUpdateServiceModel setName(String name) {
        this.name = name;
        return this;
    }


    public String getDescription() {
        return description;
    }

    public ProductUpdateServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }



    public Category getCategory() {
        return category;
    }

    public ProductUpdateServiceModel setCategory(Category category) {
        this.category = category;
        return this;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public ProductUpdateServiceModel setPictures(List<Picture> pictures) {
        this.pictures = pictures;
        return this;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public ProductUpdateServiceModel setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public ProductUpdateServiceModel setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
