package com.example.MyStore.model.binding;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProductPurchaseBindingModel {


    private Integer quantity;


    @NotNull
    @Positive(message = "Quantity must be above 0.")
    public Integer getQuantity() {
        return quantity;
    }

    public ProductPurchaseBindingModel setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }
}
