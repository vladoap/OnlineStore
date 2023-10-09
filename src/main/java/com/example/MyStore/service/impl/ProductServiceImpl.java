package com.example.MyStore.service.impl;

import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.enums.CategoryNameEnum;
import com.example.MyStore.model.service.ProductSummaryServiceModel;
import com.example.MyStore.repository.ProductRepository;
import com.example.MyStore.service.PictureService;
import com.example.MyStore.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
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

    @Override
    public List<ProductSummaryServiceModel> getAllProductsExceptOwn(String username) {
        return productRepository
                .findAllBySellerUsernameIsNot(username)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }



    @Override
    public List<ProductSummaryServiceModel> getAllProductsExceptOwnPageable(int page, int pageSize, String username) {

        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        return productRepository.findAllBySellerUsernameIsNotByPagination(username, pageRequest)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductSummaryServiceModel> getAllProductsByCategoryExceptOwnPageable(int page, int pageSize, String username, String categoryName) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        return productRepository.findAllByCategoryAndSellerUsernameIsNotByPagination(username, CategoryNameEnum.valueOf(categoryName), pageRequest)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductSummaryServiceModel> getAllProductsByCategoryExceptOwn(String username, String categoryName) {
        return productRepository
                .findAllByCategoryNameAndSellerUsernameIsNot(CategoryNameEnum.valueOf(categoryName), username)
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
