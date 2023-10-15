package com.example.MyStore.model.service;

public class UserDetailsForAdminServiceModel {

    private String profilePicture;
    private String fullName;
    private String username;
    private String email;
    private Integer productsCount;
    private Integer ordersCount;

    public String getProfilePicture() {
        return profilePicture;
    }

    public UserDetailsForAdminServiceModel setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public UserDetailsForAdminServiceModel setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDetailsForAdminServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public Integer getProductsCount() {
        return productsCount;
    }

    public UserDetailsForAdminServiceModel setProductsCount(Integer productsCount) {
        this.productsCount = productsCount;
        return this;
    }

    public Integer getOrdersCount() {
        return ordersCount;
    }

    public UserDetailsForAdminServiceModel setOrdersCount(Integer ordersCount) {
        this.ordersCount = ordersCount;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDetailsForAdminServiceModel setEmail(String email) {
        this.email = email;
        return this;
    }
}
