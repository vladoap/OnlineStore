package com.example.MyStore.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return Objects.equals(title, picture.title) && Objects.equals(url, picture.url) && Objects.equals(publicId, picture.publicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, url, publicId);
    }
}
