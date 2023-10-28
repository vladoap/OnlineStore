package com.example.MyStore.service;

import com.example.MyStore.model.entity.Cart;
import com.example.MyStore.model.entity.CartItem;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.service.ProductDetailsServiceModel;

public interface CartService {


    Cart findCartById(Long id);


    void deleteAllCartItems(Cart cart);

    void addOrUpdateCartItem(Cart cart, ProductDetailsServiceModel product, Integer quantity);

    void save(Cart cart);

    void deleteCartItemByProductId(Long productId, Cart userCart);

    Cart createNewCart();


    void deleteCartItem(CartItem cartItem);
}
