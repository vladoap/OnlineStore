package com.example.MyStore.service;

import com.example.MyStore.exception.DefaultPictureNotFoundException;
import com.example.MyStore.model.entity.Picture;

import java.io.FileNotFoundException;

public interface PictureService {

    Picture getDefaultPicture();
}
