package com.example.MyStore.model.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "carts")
public class Cart extends BaseEntity{


    private Set<CartItem> cartItems;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public Cart setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
        return this;
    }



}
