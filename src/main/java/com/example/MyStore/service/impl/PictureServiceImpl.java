package com.example.MyStore.service.impl;

import com.cloudinary.Cloudinary;
import com.example.MyStore.exception.DefaultPictureNotFoundException;
import com.example.MyStore.exception.PictureNotFoundException;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.service.PictureAddServiceModel;
import com.example.MyStore.repository.PictureRepository;
import com.example.MyStore.service.CloudinaryService;
import com.example.MyStore.service.PictureService;
import com.example.MyStore.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {

    private static final String DEFAULT_PRODUCT_PICTURE_TITLE = "Default Product";

    private final PictureRepository pictureRepository;
    private final ProductService productService;
    private final CloudinaryService cloudinaryService;


    public PictureServiceImpl(PictureRepository pictureRepository, ProductService productService, CloudinaryService cloudinaryService) {
        this.pictureRepository = pictureRepository;
        this.productService = productService;
        this.cloudinaryService = cloudinaryService;

    }

    @Override
    public Picture getDefaultPicture() {
        return pictureRepository
                .findPictureByTitle(DEFAULT_PRODUCT_PICTURE_TITLE)
                .orElseThrow(() -> new DefaultPictureNotFoundException("Default picture not found."));
    }

    @Override
    public Picture findByUrl(String imageUrl) {
        return pictureRepository
                .findPictureByUrl(imageUrl)
                .orElseThrow(() -> new PictureNotFoundException("Picture with URL: " + imageUrl + " not found."));
    }

    @Override
    public void savePicture(Long productId, PictureAddServiceModel pictureModel) {
        Product product = productService.getProductById(productId);

        Picture picture = new Picture()
                .setUrl(pictureModel.getUrl())
                .setTitle(pictureModel.getTitle())
                .setPublicId(pictureModel.getPublicId());
        picture.setCreated(LocalDateTime.now());

        pictureRepository.save(picture);

        product.getPictures().add(picture);
        productService.saveProduct(product);

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
