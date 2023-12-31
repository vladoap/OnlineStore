package com.example.MyStore.model.binding;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public class ProductAddBindingModel {

    private String name;
    private String description;
    private String category;
    private Integer quantity;
    private BigDecimal price;


    @NotBlank
    @Size(min = 3, message = "Name must be at least 3 symbols.")
    public String getName() {
        return name;
    }

    public ProductAddBindingModel setName(String name) {
        this.name = name;
        return this;
    }

    @NotBlank
    @Size(min = 5, message = "Description must be at least 3 symbols.")
    public String getDescription() {
        return description;
    }

    public ProductAddBindingModel setDescription(String description) {
        this.description = description;
        return this;
    }

    @NotNull(message = "Select category.")
    public String getCategory() {
        return category;
    }

    public ProductAddBindingModel setCategory(String category) {
        this.category = category;
        return this;
    }

    @NotNull
    @Min(value = 1, message = "Quantity must be at least 1.")
    public Integer getQuantity() {
        return quantity;
    }

    public ProductAddBindingModel setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    @NotNull
    @Positive(message = "Price must be positive.")
    public BigDecimal getPrice() {
        return price;
    }

    public ProductAddBindingModel setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
