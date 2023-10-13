package com.example.MyStore.service;

import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.service.PictureAddServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PictureService {

    Picture getDefaultProductPicture();


    Picture findByUrl(String imageUrl);


    void deletePictures(List<Picture> deletedPictures);

    Picture getDefaultProfilePicture();

    Picture uploadPicture(MultipartFile file, String title) throws IOException;



}
