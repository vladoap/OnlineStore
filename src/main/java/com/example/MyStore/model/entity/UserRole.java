package com.example.MyStore.model.entity;

import com.example.MyStore.model.enums.UserRoleEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class UserRole {


    private Long id;
    private UserRoleEnum name;

    public UserRole() {
    }

    @Column
    @Enumerated(EnumType.STRING)
    public UserRoleEnum getName() {
        return name;
    }

    public UserRole setName(UserRoleEnum userRoleEnum) {
        this.name = userRoleEnum;
        return this;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public UserRole setId(Long id) {
        this.id = id;
        return this;
    }
}
