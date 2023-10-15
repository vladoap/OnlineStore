package com.example.MyStore.service;

import com.example.MyStore.model.entity.Cart;
import com.example.MyStore.model.entity.CartItem;
import com.example.MyStore.model.entity.User;
import com.example.MyStore.model.service.ProductDetailsServiceModel;

import java.util.Optional;

public interface CartItemService {
    void save(CartItem cartItemToAdd);

    CartItem createNewCartItem(Integer quantity, ProductDetailsServiceModel product);


    void deleteById(Long id);

    void delete(CartItem cartItem);
}
