package com.example.MyStore.service.impl;

import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.service.ProductSummaryServiceModel;
import com.example.MyStore.repository.ProductRepository;
import com.example.MyStore.service.PictureService;
import com.example.MyStore.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final PictureService pictureService;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, PictureService pictureService) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.pictureService = pictureService;
    }

    @Override
    public List<ProductSummaryServiceModel> getAllProducts() {
     return  productRepository
                .findAll()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private ProductSummaryServiceModel map(Product product) {
        ProductSummaryServiceModel productServiceModel = modelMapper.map(product, ProductSummaryServiceModel.class);

        Picture picture = product.getPictures().stream().findFirst().orElse(pictureService.getDefaultPicture());

        productServiceModel.setImageUrl(picture.getUrl());

        return productServiceModel;
    }
}
