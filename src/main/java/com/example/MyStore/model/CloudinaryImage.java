package com.example.MyStore.model;

public class CloudinaryImage {

    private String publicId;
    private String url;

    public String getPublicId() {
        return publicId;
    }

    public CloudinaryImage setPublicId(String publicId) {
        this.publicId = publicId;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public CloudinaryImage setUrl(String url) {
        this.url = url;
        return this;
    }
}
