package com.example.MyStore.service;

import com.example.MyStore.model.service.UserRegisterServiceModel;

public interface UserService {
    boolean isUsernameFree(String username);

    void registerUser(UserRegisterServiceModel userModel);
}
