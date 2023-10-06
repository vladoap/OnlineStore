package com.example.MyStore.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pictures")
public class Picture extends BaseEntity {

    private String title;
    private String url;
    private String publicId;


    @Column(nullable = false)
    public String getTitle() {
        return title;
    }

    public Picture setTitle(String title) {
        this.title = title;
        return this;
    }

    @Column(nullable = false, unique = true)
    public String getUrl() {
        return url;
    }

    public Picture setUrl(String url) {
        this.url = url;
        return this;
    }

    @Column(name = "public_id", unique = true)
    public String getPublicId() {
        return publicId;
    }

    public Picture setPublicId(String publicId) {
        this.publicId = publicId;
        return this;
    }


}
