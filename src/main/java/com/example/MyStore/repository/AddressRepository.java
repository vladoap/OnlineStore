package com.example.MyStore.repository;

import com.example.MyStore.model.entity.Address;
import com.example.MyStore.model.entity.AddressId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, AddressId> {
}
