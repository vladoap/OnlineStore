package com.example.MyStore.service;

import com.example.MyStore.model.entity.CartItem;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.entity.User;
import com.example.MyStore.model.service.UserRegisterServiceModel;

import java.util.Optional;

public interface UserService {
    boolean isUsernameFree(String username);

    void registerUser(UserRegisterServiceModel userModel);

    User findByUsername(String username);

    void addCartItemInUserCart(Integer quantity, String buyerUsername, Product product);

    Optional<CartItem> getCartItemByProductAndUsername(Product product, String username);

    Integer getCartItemQuantity(Product product, String username);
}
