package com.example.MyStore.service;

import com.example.MyStore.model.entity.OrderItem;

import java.util.List;

public interface OrderItemService {
    void save(List<OrderItem> products);
}
