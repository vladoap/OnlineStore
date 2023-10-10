package com.example.MyStore.model.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity{

    private User buyer;
    private String internalOrderNumber;





    @ManyToOne(optional = false)
    public User getBuyer() {
        return buyer;
    }

    public Order setBuyer(User buyer) {
        this.buyer = buyer;
        return this;
    }


    @Column(name = "internal_order_number")
    public String getInternalOrderNumber() {
        return internalOrderNumber;
    }

    public Order setInternalOrderNumber(String internalOrderNumber) {
        this.internalOrderNumber = internalOrderNumber;
        return this;
    }
}
