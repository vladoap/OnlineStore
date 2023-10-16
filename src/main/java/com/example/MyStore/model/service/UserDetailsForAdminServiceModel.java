package com.example.MyStore.model.service;

import com.example.MyStore.model.enums.UserRoleEnum;

public class UserDetailsForAdminServiceModel {

    private Long id;
    private String profilePicture;
    private String fullName;
    private String username;
    private UserRoleEnum role;
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

    public UserRoleEnum getRole() {
        return role;
    }

    public UserDetailsForAdminServiceModel setRole(UserRoleEnum role) {
        this.role = role;
        return this;
    }

    public Long getId() {
        return id;
    }

    public UserDetailsForAdminServiceModel setId(Long id) {
        this.id = id;
        return this;
    }
}
