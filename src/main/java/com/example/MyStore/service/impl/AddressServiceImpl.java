package com.example.MyStore.service.impl;

import com.example.MyStore.model.entity.Address;
import com.example.MyStore.model.entity.AddressId;
import com.example.MyStore.repository.AddressRepository;
import com.example.MyStore.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Optional<Address> findById(AddressId addressId) {
        return addressRepository.findById(addressId);
    }

    @Override
    public void save(Address address) {
        addressRepository.save(address);
    }
}
