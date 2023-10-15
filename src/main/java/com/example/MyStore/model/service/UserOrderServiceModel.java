package com.example.MyStore.model.service;

public class UserOrderServiceModel {

    private String recipientFirstName;
    private String recipientLastName;
    private String recipientEmail;
    private String country;
    private String city;
    private String streetName;
    private Integer streetNumber;

    public String getRecipientFirstName() {
        return recipientFirstName;
    }

    public UserOrderServiceModel setRecipientFirstName(String recipientFirstName) {
        this.recipientFirstName = recipientFirstName;
        return this;
    }

    public String getRecipientLastName() {
        return recipientLastName;
    }

    public UserOrderServiceModel setRecipientLastName(String recipientLastName) {
        this.recipientLastName = recipientLastName;
        return this;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public UserOrderServiceModel setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public UserOrderServiceModel setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCity() {
        return city;
    }

    public UserOrderServiceModel setCity(String city) {
        this.city = city;
        return this;
    }

    public String getStreetName() {
        return streetName;
    }

    public UserOrderServiceModel setStreetName(String streetName) {
        this.streetName = streetName;
        return this;
    }

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public UserOrderServiceModel setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }
}
