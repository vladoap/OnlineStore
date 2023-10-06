package com.example.MyStore.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "addresses")
public class Address {


    private AddressId id;
    private String country;



    @Column(nullable = false)
    public String getCountry() {
        return country;
    }

    public Address setCountry(String country) {
        this.country = country;
        return this;
    }

    @EmbeddedId
    public AddressId getId() {
        return id;
    }

    public Address setId(AddressId id) {
        this.id = id;
        return this;
    }
}
