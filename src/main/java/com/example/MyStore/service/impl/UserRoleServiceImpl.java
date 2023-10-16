package com.example.MyStore.service.impl;

import com.example.MyStore.model.entity.UserRole;
import com.example.MyStore.model.enums.UserRoleEnum;
import com.example.MyStore.repository.UserRoleRepository;
import com.example.MyStore.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }


    @Override
    public UserRole getUserRole() {
        return userRoleRepository.findByName(UserRoleEnum.USER).orElseThrow(() -> new IllegalArgumentException("Role 'User' doesn't exist."));
    }

    @Override
    public UserRole getAdminRole() {
        return userRoleRepository.findByName(UserRoleEnum.ADMIN).orElseThrow(() -> new IllegalArgumentException("Role 'Admin' doesn't exist."));
    }
}
