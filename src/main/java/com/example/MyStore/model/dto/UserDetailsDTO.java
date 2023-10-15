package com.example.MyStore.model.dto;

public class UserDetailsDTO {

    private String profilePicture;
    private String fullName;
    private String username;
    private String email;
    private Integer productsCount;
    private Integer ordersCount;

    public String getProfilePicture() {
        return profilePicture;
    }

    public UserDetailsDTO setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public UserDetailsDTO setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDetailsDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public Integer getProductsCount() {
        return productsCount;
    }

    public UserDetailsDTO setProductsCount(Integer productsCount) {
        this.productsCount = productsCount;
        return this;
    }

    public Integer getOrdersCount() {
        return ordersCount;
    }

    public UserDetailsDTO setOrdersCount(Integer ordersCount) {
        this.ordersCount = ordersCount;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDetailsDTO setEmail(String email) {
        this.email = email;
        return this;
    }
}
