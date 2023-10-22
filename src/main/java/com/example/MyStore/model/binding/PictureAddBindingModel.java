package com.example.MyStore.model.binding;

import jakarta.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

public class PictureAddBindingModel {


    private MultipartFile picture;


    @NotNull
    public MultipartFile getPicture() {
        return picture;
    }

    public PictureAddBindingModel setPicture(MultipartFile picture) {
        this.picture = picture;
        return this;
    }
}
