package com.example.MyStore.model.view;

import com.example.MyStore.model.entity.*;
import com.example.MyStore.model.enums.CategoryNameEnum;
import com.example.MyStore.model.enums.TitleEnum;

import java.math.BigDecimal;
import java.util.List;

public class ProductDetailsViewModel {

    private Long id;
    private String name;
    private String imageUrl;
    private String description;
    private String seller;
    private CategoryNameEnum category;
    private List<String> pictures;
    private Integer quantity;
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public ProductDetailsViewModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductDetailsViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductDetailsViewModel setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductDetailsViewModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getSeller() {
        return seller;
    }

    public ProductDetailsViewModel setSeller(String seller) {
        this.seller = seller;
        return this;
    }

    public CategoryNameEnum getCategory() {
        return category;
    }

    public ProductDetailsViewModel setCategory(CategoryNameEnum category) {
        this.category = category;
        return this;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public ProductDetailsViewModel setPictures(List<String> pictures) {
        this.pictures = pictures;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductDetailsViewModel setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductDetailsViewModel setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
