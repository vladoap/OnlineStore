package com.example.MyStore.service.impl;

import com.example.MyStore.exception.CartNotFoundException;
import com.example.MyStore.model.entity.Cart;
import com.example.MyStore.model.entity.CartItem;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.service.ProductDetailsServiceModel;
import com.example.MyStore.repository.CartRepository;
import com.example.MyStore.service.CartItemService;
import com.example.MyStore.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;


    public CartServiceImpl(CartRepository cartRepository, CartItemService cartItemService) {
        this.cartRepository = cartRepository;

        this.cartItemService = cartItemService;
    }


    @Override
    public Cart findCartById(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new CartNotFoundException("Cart with ID: " + id + " not found."));
    }


    @Override
    public void deleteAllCartItems(Cart cart) {
        for (CartItem cartItem : cart.getCartItems()) {
            cartItemService.delete(cartItem);
        }

        cart.getCartItems().clear();
    }


    @Override
    public void addOrUpdateCartItem(Cart cart, ProductDetailsServiceModel product, Integer quantity) {

        Optional<CartItem> cartItemOpt = cart.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProductId().equals(product.getId()))
                .findFirst();

        if (cartItemOpt.isEmpty()) {
            CartItem cartItem = cartItemService.createNewCartItem(quantity, product);
            cart.getCartItems().add(cartItem);

        } else {
            CartItem existingCartItem = cartItemOpt.get();
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
        }

    }

    @Override
    public void save(Cart cart) {
        cartRepository.save(cart);
    }




    @Override
    public void deleteCartItemByProductId(Long productId, Cart cart) {

       CartItem cartItemToDelete = cart.getCartItems().stream().filter(cartItem -> cartItem.getProductId().equals(productId))
                .findFirst().orElseThrow(() -> new CartNotFoundException("Cart item not found in the cart."));

       cart.getCartItems().remove(cartItemToDelete);

       cartItemService.delete(cartItemToDelete);
    }

    @Override
    public Cart createNewCart() {
        Cart cart = new Cart().setCartItems(new HashSet<>());
        cart.setCreated(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCartItem(CartItem cartItem) {
        cartItemService.delete(cartItem);
    }






}
