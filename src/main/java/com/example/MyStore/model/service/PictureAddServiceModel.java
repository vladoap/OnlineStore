package com.example.MyStore.model.service;

public class PictureAddServiceModel {

    private String title;
    private String url;
    private String publicId;

    public String getTitle() {
        return title;
    }

    public PictureAddServiceModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public PictureAddServiceModel setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getPublicId() {
        return publicId;
    }

    public PictureAddServiceModel setPublicId(String publicId) {
        this.publicId = publicId;
        return this;
    }
}
