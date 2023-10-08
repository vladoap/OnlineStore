package com.example.MyStore.repository;

import com.example.MyStore.model.entity.UserRole;
import com.example.MyStore.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    Optional<UserRole> findByName(UserRoleEnum name);
}
