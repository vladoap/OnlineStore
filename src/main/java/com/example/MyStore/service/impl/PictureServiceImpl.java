package com.example.MyStore.service.impl;

import com.example.MyStore.exception.DefaultPictureNotFoundException;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.repository.PictureRepository;
import com.example.MyStore.service.PictureService;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public class PictureServiceImpl implements PictureService {

    private static final String DEFAULT_PRODUCT_PICTURE_TITLE = "Default Product";

    private final PictureRepository pictureRepository;

    public PictureServiceImpl(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @Override
    public Picture getDefaultPicture() {
        return pictureRepository.findPictureByTitle(DEFAULT_PRODUCT_PICTURE_TITLE).orElseThrow(() -> new DefaultPictureNotFoundException("Default picture not found."));
    }
}
