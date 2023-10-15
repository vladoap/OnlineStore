package com.example.MyStore.service;

import com.example.MyStore.model.entity.Order;
import com.example.MyStore.model.service.UserOrderServiceModel;

public interface OrderService {

    void save(Order order);
}
