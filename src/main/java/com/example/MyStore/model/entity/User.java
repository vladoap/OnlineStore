package com.example.MyStore.model.entity;

import com.example.MyStore.model.enums.UserRoleEnum;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity{

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Picture profilePicture;
    private Set<UserRole> userRoleEnums;
    private Set<Product> products;
    private Address address;


    @Column(nullable = false, unique = true)
    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    @Column(nullable = false)
    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @Column(nullable = false, name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    @Column(nullable = false, name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Column
    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }


    @OneToOne
    public Picture getProfilePicture() {
        return profilePicture;
    }

    public User setProfilePicture(Picture profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    public Set<UserRole> getRoles() {
        return userRoleEnums;
    }

    public User setRoles(Set<UserRole> userRoleEnums) {
        this.userRoleEnums = userRoleEnums;
        return this;
    }

    @OneToMany(mappedBy = "seller" ,fetch = FetchType.EAGER)
    public Set<Product> getProducts() {
        return products;
    }

    public User setProducts(Set<Product> products) {
        this.products = products;
        return this;
    }

    @ManyToOne
    public Address getAddress() {
        return address;
    }

    public User setAddress(Address address) {
        this.address = address;
        return this;
    }
}