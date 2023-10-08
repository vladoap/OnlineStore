package com.example.MyStore.model.service;

import com.example.MyStore.model.entity.Address;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.entity.UserRole;
import com.example.MyStore.model.enums.TitleEnum;

import java.util.Set;

public class UserRegisterServiceModel {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private TitleEnum title;
    private Picture profilePicture;
    private Set<UserRole> role;
    private Set<Product> products;
    private String country;
    private String city;
    private String streetName;
    private Integer streetNumber;

    public String getUsername() {
        return username;
    }

    public UserRegisterServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRegisterServiceModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserRegisterServiceModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRegisterServiceModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRegisterServiceModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public TitleEnum getTitle() {
        return title;
    }

    public UserRegisterServiceModel setTitle(TitleEnum title) {
        this.title = title;
        return this;
    }

    public Picture getProfilePicture() {
        return profilePicture;
    }

    public UserRegisterServiceModel setProfilePicture(Picture profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    public Set<UserRole> getRole() {
        return role;
    }

    public UserRegisterServiceModel setRole(Set<UserRole> role) {
        this.role = role;
        return this;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public UserRegisterServiceModel setProducts(Set<Product> products) {
        this.products = products;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public UserRegisterServiceModel setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCity() {
        return city;
    }

    public UserRegisterServiceModel setCity(String city) {
        this.city = city;
        return this;
    }

    public String getStreetName() {
        return streetName;
    }

    public UserRegisterServiceModel setStreetName(String streetName) {
        this.streetName = streetName;
        return this;
    }

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public UserRegisterServiceModel setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }
}
