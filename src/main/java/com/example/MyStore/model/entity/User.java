package com.example.MyStore.model.entity;

import com.example.MyStore.model.enums.TitleEnum;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity{

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private TitleEnum title;
    private Picture profilePicture;
    private Set<UserRole> roles;
    private Set<Product> products;
    private Cart cart;
    private Address address;
    private List<Order> orders;



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


    @ManyToOne(cascade = CascadeType.REMOVE)
    public Picture getProfilePicture() {
        return profilePicture;
    }

    public User setProfilePicture(Picture profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    public Set<UserRole> getRoles() {
        return roles;
    }

    public User setRoles(Set<UserRole> userRoleEnums) {
        this.roles = userRoleEnums;
        return this;
    }

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "seller", fetch = FetchType.EAGER)
    public Set<Product> getProducts() {
        return products;
    }

    public User setProducts(Set<Product> products) {
        this.products = products;
        return this;
    }

    @ManyToOne(cascade = CascadeType.REMOVE)
    public Address getAddress() {
        return address;
    }

    public User setAddress(Address address) {
        this.address = address;
        return this;
    }

    @Column
    @Enumerated(EnumType.STRING)
    public TitleEnum getTitle() {
        return title;
    }

    public User setTitle(TitleEnum title) {
        this.title = title;
        return this;
    }



   @OneToOne(cascade = CascadeType.REMOVE)
    public Cart getCart() {
        return cart;
    }

    public User setCart(Cart cart) {
        this.cart = cart;
        return this;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    public List<Order> getOrders() {
        return orders;
    }

    public User setOrders(List<Order> orders) {
        this.orders = orders;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
