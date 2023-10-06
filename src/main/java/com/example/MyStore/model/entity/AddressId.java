package com.example.MyStore.model.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class AddressId implements Serializable {

    private String streetName;
    private String city;
    private Integer streetNumber;

    public String getStreetName() {
        return streetName;
    }

    public AddressId setStreetName(String streetName) {
        this.streetName = streetName;
        return this;
    }

    public String getCity() {
        return city;
    }

    public AddressId setCity(String city) {
        this.city = city;
        return this;
    }

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public AddressId setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }
}
