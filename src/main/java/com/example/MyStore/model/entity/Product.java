package com.example.MyStore.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {


    private String name;
    private String description;
    private User seller;
    private Category category;
    private Set<Picture> pictures;
    private Integer quantity;
    private BigDecimal price;

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    @Column(columnDefinition = "text", nullable = false)
    public String getDescription() {
        return description;
    }

    public Product setDescription(String description) {
        this.description = description;
        return this;
    }

    @Column(nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public Product setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    @Column(nullable = false)
    public BigDecimal getPrice() {
        return price;
    }

    public Product setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    @ManyToOne(optional = false)
    public User getSeller() {
        return seller;
    }

    public Product setSeller(User seller) {
        this.seller = seller;
        return this;
    }

    @ManyToOne(optional = false)
    public Category getCategory() {
        return category;
    }

    public Product setCategory(Category category) {
        this.category = category;
        return this;
    }

    @OneToMany(fetch = FetchType.EAGER)
    public Set<Picture> getPictures() {
        return pictures;
    }

    public Product setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
        return this;
    }
}
