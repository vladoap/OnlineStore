package com.example.MyStore.model.binding;

import com.example.MyStore.model.entity.Category;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.entity.User;

import java.math.BigDecimal;
import java.util.List;

public class ProductAddBindingModel {


    private Long id;
    private String name;
    private String description;
    private String category;
    private List<String> pictures;
    private Integer quantity;
    private BigDecimal price;

    public String getName() {
        return name;
    }

    public ProductAddBindingModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductAddBindingModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public ProductAddBindingModel setCategory(String category) {
        this.category = category;
        return this;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public ProductAddBindingModel setPictures(List<String> pictures) {
        this.pictures = pictures;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductAddBindingModel setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductAddBindingModel setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Long getId() {
        return id;
    }

    public ProductAddBindingModel setId(Long id) {
        this.id = id;
        return this;
    }
}
