package com.example.MyStore.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity{

    private String recipientFirstName;
    private String recipientLastName;
    private String recipientEmail;
    private List<OrderItem> orderItems;
    private Address deliveryAddress;
    private BigDecimal totalPrice;

    @Column(name = "recipient_first_name", nullable = false)
    public String getRecipientFirstName() {
        return recipientFirstName;
    }

    public Order setRecipientFirstName(String recipientFirstName) {
        this.recipientFirstName = recipientFirstName;
        return this;
    }

    @Column(name = "recipient_last_name", nullable = false)
    public String getRecipientLastName() {
        return recipientLastName;
    }

    public Order setRecipientLastName(String recipientLastName) {
        this.recipientLastName = recipientLastName;
        return this;
    }

    @Column(name = "recipient_email")
    public String getRecipientEmail() {
        return recipientEmail;
    }

    public Order setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
        return this;
    }

    @Column(name = "total_price")
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public Order setTotalPrice(BigDecimal totalSum) {
        this.totalPrice = totalSum;
        return this;
    }

    @ManyToOne(cascade = CascadeType.REMOVE)
    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public Order setDeliveryAddress(Address shippingAddress) {
        this.deliveryAddress = shippingAddress;
        return this;
    }



    @ManyToMany(cascade = CascadeType.REMOVE)
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Order setOrderItems(List<OrderItem> products) {
        this.orderItems = products;
        return this;
    }
}
