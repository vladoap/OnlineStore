package com.example.MyStore.service.impl;

import com.example.MyStore.model.entity.OrderItem;
import com.example.MyStore.repository.OrderItemRepository;
import com.example.MyStore.service.OrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public void save(List<OrderItem> products) {
        orderItemRepository.saveAll(products);
    }
}
