package com.example.MyStore.model.binding;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class PictureAddBindingModel {


    private String title;
    private MultipartFile picture;

    @NotNull
    @Size(min = 3)
    public String getTitle() {
        return title;
    }

    public PictureAddBindingModel setTitle(String title) {
        this.title = title;
        return this;
    }

    @NotNull
    public MultipartFile getPicture() {
        return picture;
    }

    public PictureAddBindingModel setPicture(MultipartFile picture) {
        this.picture = picture;
        return this;
    }
}
