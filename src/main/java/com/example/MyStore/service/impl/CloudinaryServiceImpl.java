package com.example.MyStore.service.impl;

import com.cloudinary.Cloudinary;
import com.example.MyStore.model.CloudinaryImage;
import com.example.MyStore.service.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private static final String CLOUDINARY_PUBLIC_ID_NAME = "public_id";
    private static final String CLOUDINARY_URL_NAME = "url";
    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public CloudinaryImage upload(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("temp-file", file.getOriginalFilename());
        file.transferTo(tempFile);

        Map<String, String> uploadResult = cloudinary.uploader().upload(tempFile, Map.of());

        String publicId = uploadResult.getOrDefault(CLOUDINARY_PUBLIC_ID_NAME, "");
        String url = uploadResult.getOrDefault(CLOUDINARY_URL_NAME, "https://static.vecteezy.com/system/resources/previews/005/337/799/original/icon-image-not-found-free-vector.jpg");

        return new CloudinaryImage()
                .setPublicId(publicId)
                .setUrl(url);
    }

    @Override
    public boolean delete(String publicId) throws IOException {
        try {
            cloudinary.uploader().destroy(publicId, Map.of());

        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
