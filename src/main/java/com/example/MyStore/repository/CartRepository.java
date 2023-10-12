package com.example.MyStore.repository;

import com.example.MyStore.model.entity.Cart;
import com.example.MyStore.model.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {



}
