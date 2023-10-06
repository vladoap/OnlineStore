package com.example.MyStore.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity{

    private User seller;
    private User buyer;
    private Address deliveryAddress;
    private Set<Product> products;

    @ManyToOne(optional = false)
    public User getSeller() {
        return seller;
    }

    public Order setSeller(User seller) {
        this.seller = seller;
        return this;
    }

    @ManyToOne(optional = false)
    public User getBuyer() {
        return buyer;
    }

    public Order setBuyer(User buyer) {
        this.buyer = buyer;
        return this;
    }

    @ManyToOne(optional = false)
    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public Order setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        return this;
    }

    @ManyToMany
    public Set<Product> getProducts() {
        return products;
    }

    public Order setProducts(Set<Product> products) {
        this.products = products;
        return this;
    }
}
