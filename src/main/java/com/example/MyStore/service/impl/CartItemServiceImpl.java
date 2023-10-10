package com.example.MyStore.service.impl;

import com.example.MyStore.model.entity.CartItem;
import com.example.MyStore.repository.CartItemRepository;
import com.example.MyStore.service.CartItemService;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public void save(CartItem cartItemToAdd) {
        cartItemRepository.save(cartItemToAdd);
    }
}
