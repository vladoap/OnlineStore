package com.example.MyStore.service.impl;

import com.example.MyStore.model.entity.CartItem;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.service.ProductDetailsServiceModel;
import com.example.MyStore.repository.CartItemRepository;
import com.example.MyStore.service.CartItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    @Override
    public CartItem createNewCartItem(Integer quantity, ProductDetailsServiceModel product) {
        CartItem cartItemToAdd = new CartItem()
                .setName(product.getName())
                .setProductId(product.getId())
                .setPrice(product.getPrice())
                .setQuantity(quantity)
                .setImageUrl(product.getPictures()
                        .stream().findFirst().map(Picture::getUrl).orElse(null));
        cartItemToAdd.setCreated(LocalDateTime.now());

        return cartItemRepository.save(cartItemToAdd);

    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }


    @Override
    public void delete(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }


}
