package com.example.MyStore.service.impl;



import com.example.MyStore.model.entity.Order;
import com.example.MyStore.repository.OrderRepository;
import com.example.MyStore.service.OrderItemService;
import com.example.MyStore.service.OrderService;
import org.springframework.stereotype.Service;


@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemService orderItemService) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
    }

    @Override
    public void save(Order order) {
        orderItemService.save(order.getOrderItems());

        orderRepository.save(order);
    }
}