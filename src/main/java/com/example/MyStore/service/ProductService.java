package com.example.MyStore.service;

import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.service.ProductAddServiceModel;
import com.example.MyStore.model.service.ProductUpdateServiceModel;
import com.example.MyStore.model.service.ProductDetailsServiceModel;
import com.example.MyStore.model.service.ProductSummaryServiceModel;

import java.util.List;

public interface ProductService {
    List<ProductSummaryServiceModel> getAllProducts();



    List<ProductSummaryServiceModel> getAllProductsExceptOwnPageable(int page, int pageSize, String username);

    List<ProductSummaryServiceModel> getAllProductsByCategoryExceptOwn(String username, String categoryName);

    List<ProductSummaryServiceModel> getAllProductsByCategoryExceptOwnPageable(int page, int pageSize, String username, String categoryName);

    List<ProductSummaryServiceModel> getAllProductsExceptOwn(String username);

    List<ProductSummaryServiceModel> getAllProductsForUserPageable(int page, int pageSize, String username);

    List<ProductSummaryServiceModel> getAllProductsForUser(String username);

    ProductDetailsServiceModel getById(Long id);

    Integer getAvailableQuantityById(Long id);

    List<ProductSummaryServiceModel> getTheLatestThreeProducts();


    ProductUpdateServiceModel findById(Long id);

    Product getProductById(Long id);

    List<Picture> updateProduct(ProductUpdateServiceModel productUpdateServiceModel);

    void deleteProduct(Long id);

    void saveProduct(Product product);


    void addProduct(ProductAddServiceModel productServiceModel, String username);
}
