package com.example.MyStore.service;

import com.example.MyStore.exception.DefaultPictureNotFoundException;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.service.PictureAddServiceModel;

import java.io.FileNotFoundException;
import java.util.List;

public interface PictureService {

    Picture getDefaultPicture();


    Picture findByUrl(String imageUrl);

    void savePicture(Long productId, PictureAddServiceModel pictureAddServiceModel);

    void deletePictures(List<Picture> deletedPictures);
}
