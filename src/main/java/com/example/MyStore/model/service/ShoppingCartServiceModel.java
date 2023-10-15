package com.example.MyStore.model.service;

import java.math.BigDecimal;
import java.util.List;

public class ShoppingCartServiceModel {

    List<CartItemServiceModel> cartItems;

    public List<CartItemServiceModel> getCartItems() {
        return cartItems;
    }

    public ShoppingCartServiceModel setCartItems(List<CartItemServiceModel> cartItems) {
        this.cartItems = cartItems;
        return this;
    }
}
