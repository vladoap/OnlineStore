package com.example.MyStore.model.service;

import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.entity.UserRole;
import com.example.MyStore.model.enums.TitleEnum;

import java.util.Set;

public class UserDetailsServiceModel {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private TitleEnum title;
    private Picture profilePicture;
    private String country;
    private String city;
    private String streetName;
    private Integer streetNumber;

    public String getUsername() {
        return username;
    }

    public UserDetailsServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserDetailsServiceModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserDetailsServiceModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDetailsServiceModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public TitleEnum getTitle() {
        return title;
    }

    public UserDetailsServiceModel setTitle(TitleEnum title) {
        this.title = title;
        return this;
    }

    public Picture getProfilePicture() {
        return profilePicture;
    }

    public UserDetailsServiceModel setProfilePicture(Picture profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public UserDetailsServiceModel setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCity() {
        return city;
    }

    public UserDetailsServiceModel setCity(String city) {
        this.city = city;
        return this;
    }

    public String getStreetName() {
        return streetName;
    }

    public UserDetailsServiceModel setStreetName(String streetName) {
        this.streetName = streetName;
        return this;
    }

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public UserDetailsServiceModel setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }
}
