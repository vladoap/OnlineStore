package com.example.MyStore.model.binding;

import jakarta.validation.constraints.*;

public class UserOrderBindingModel {

    private String country;
    private String city;
    private String streetName;
    private Integer streetNumber;
    private String recipientFirstName;
    private String recipientLastName;
    private String recipientEmail;

    @NotBlank
    @Size(min = 2, message = "Country must be at least 2 symbols.")
    public String getCountry() {
        return country;
    }

    public UserOrderBindingModel setCountry(String country) {
        this.country = country;
        return this;
    }

    @NotBlank
    @Size(min = 2, message = "City must be at least 2 symbols.")
    public String getCity() {
        return city;
    }

    public UserOrderBindingModel setCity(String city) {
        this.city = city;
        return this;
    }

    @NotBlank
    @Size(min = 3, message = "Street name must be at least 3 symbols.")
    public String getStreetName() {
        return streetName;
    }

    public UserOrderBindingModel setStreetName(String streetName) {
        this.streetName = streetName;
        return this;
    }

    @NotNull
    @Positive(message = "Enter valid street number.")
    public Integer getStreetNumber() {
        return streetNumber;
    }

    public UserOrderBindingModel setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }

    @NotBlank
    @Size(min = 2, message = "First name must be at least 2 symbols.")
    public String getRecipientFirstName() {
        return recipientFirstName;
    }

    public UserOrderBindingModel setRecipientFirstName(String recipientFirstName) {
        this.recipientFirstName = recipientFirstName;
        return this;
    }

    @NotBlank
    @Size(min = 2, message = "Last name must be at least 2 symbols.")
    public String getRecipientLastName() {
        return recipientLastName;
    }

    public UserOrderBindingModel setRecipientLastName(String recipientLastName) {
        this.recipientLastName = recipientLastName;
        return this;
    }

    @NotBlank
    @Email(message = "Enter valid email.")
    public String getRecipientEmail() {
        return recipientEmail;
    }

    public UserOrderBindingModel setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
        return this;
    }
}
