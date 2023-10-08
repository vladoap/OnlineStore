package com.example.MyStore.service;

import com.example.MyStore.model.entity.Address;
import com.example.MyStore.model.entity.AddressId;

import java.util.Optional;

public interface AddressService {

    Optional<Address> findById(AddressId addressId);

    void save(Address address);
}
