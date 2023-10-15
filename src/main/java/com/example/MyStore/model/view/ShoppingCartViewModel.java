package com.example.MyStore.model.view;

import com.example.MyStore.model.view.CartItemViewModel;

import java.util.List;

public class ShoppingCartViewModel {

   List<CartItemViewModel> cartItems;

   public List<CartItemViewModel> getCartItems() {
      return cartItems;
   }

   public ShoppingCartViewModel setCartItems(List<CartItemViewModel> cartItems) {
      this.cartItems = cartItems;
      return this;
   }
}
