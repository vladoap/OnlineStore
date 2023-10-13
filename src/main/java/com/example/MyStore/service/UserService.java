package com.example.MyStore.service;

import com.example.MyStore.model.entity.User;
import com.example.MyStore.model.service.ProductDetailsServiceModel;
import com.example.MyStore.model.service.UserDetailsServiceModel;
import com.example.MyStore.model.service.UserRegisterServiceModel;

public interface UserService {
    boolean isUsernameFree(String username);

    void registerUser(UserRegisterServiceModel userModel);

    User findByUsername(String username);

    void addCartItemInUserCart(Integer quantity, String buyerUsername, ProductDetailsServiceModel product);


    Integer getCartItemQuantityForUser(Long productId, String username);

    UserDetailsServiceModel getUserByUsername(String username
    );

    boolean isPasswordCorrectForUser(String username, String password);

    void updateUserDetails(UserDetailsServiceModel userModel, String username);
}
