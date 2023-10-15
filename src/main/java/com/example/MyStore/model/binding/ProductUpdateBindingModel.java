package com.example.MyStore.model.binding;


import com.example.MyStore.model.enums.CategoryNameEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public class ProductUpdateBindingModel {


    private Long id;
    private String name;
    private String description;
    private CategoryNameEnum category;
    private List<String> pictures;
    private Integer quantity;
    private BigDecimal price;

    @NotBlank
    @Size(min = 3, message = "Name must be at least 3 symbols.")
    public String getName() {
        return name;
    }

    public ProductUpdateBindingModel setName(String name) {
        this.name = name;
        return this;
    }


    @NotBlank
    @Size(min = 5, message = "Description must be at least 3 symbols.")
    public String getDescription() {
        return description;
    }

    public ProductUpdateBindingModel setDescription(String description) {
        this.description = description;
        return this;
    }

    @NotNull(message = "Select category.")
    @Enumerated(EnumType.STRING)
    public CategoryNameEnum getCategory() {
        return category;
    }

    public ProductUpdateBindingModel setCategory(CategoryNameEnum category) {
        this.category = category;
        return this;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public ProductUpdateBindingModel setPictures(List<String> pictures) {
        this.pictures = pictures;
        return this;
    }


    @NotNull
    @Min(value = 1, message = "Quantity must be at least 1.")
    public Integer getQuantity() {
        return quantity;
    }

    public ProductUpdateBindingModel setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    @NotNull
    @Positive(message = "Price must be positive.")
    public BigDecimal getPrice() {
        return price;
    }

    public ProductUpdateBindingModel setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Long getId() {
        return id;
    }

    public ProductUpdateBindingModel setId(Long id) {
        this.id = id;
        return this;
    }
}
