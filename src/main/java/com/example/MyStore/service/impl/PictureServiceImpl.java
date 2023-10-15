package com.example.MyStore.service.impl;

import com.example.MyStore.exception.DefaultPictureNotFoundException;
import com.example.MyStore.exception.PictureNotFoundException;
import com.example.MyStore.model.CloudinaryImage;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.service.PictureAddServiceModel;
import com.example.MyStore.repository.PictureRepository;
import com.example.MyStore.service.CloudinaryService;
import com.example.MyStore.service.PictureService;
import com.example.MyStore.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {

    private static final String DEFAULT_PRODUCT_PICTURE_TITLE = "Default Product";
    private static final String DEFAULT_PROFILE_PICTURE_TITLE = "Default Profile";

    private final PictureRepository pictureRepository;
    private final CloudinaryService cloudinaryService;


    public PictureServiceImpl(PictureRepository pictureRepository,  CloudinaryService cloudinaryService) {
        this.pictureRepository = pictureRepository;
        this.cloudinaryService = cloudinaryService;

    }

    @Override
    public Picture getDefaultProductPicture() {
        return pictureRepository
                .findPictureByTitle(DEFAULT_PRODUCT_PICTURE_TITLE)
                .orElseThrow(() -> new DefaultPictureNotFoundException("Default product picture not found."));
    }

    @Override
    public Picture getDefaultProfilePicture() {
        return pictureRepository
                .findPictureByTitle(DEFAULT_PROFILE_PICTURE_TITLE)
                .orElseThrow(() -> new DefaultPictureNotFoundException("Default profile picture not found."));
    }

    @Override
    public Picture uploadPicture(MultipartFile file, String title) throws IOException {
        CloudinaryImage cloudinaryImage = cloudinaryService.upload(file);

        Picture picture = new Picture()
                .setUrl(cloudinaryImage.getUrl())
                .setPublicId(cloudinaryImage.getPublicId())
                .setTitle(title);
        picture.setCreated(LocalDateTime.now());

        pictureRepository.save(picture);

        return picture;
    }


    @Override
    public Picture findByUrl(String imageUrl) {
        return pictureRepository
                .findPictureByUrl(imageUrl)
                .orElseThrow(() -> new PictureNotFoundException("Picture with URL: " + imageUrl + " not found."));
    }


    @Transactional
    @Override
    public void deletePictures(List<Picture> deletedPictures) {
        if (deletedPictures.isEmpty()) {
            return;
        }

        pictureRepository.deleteAll(deletedPictures);

        deletedPictures.forEach(pic -> {
            try {
                cloudinaryService.delete(pic.getPublicId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
