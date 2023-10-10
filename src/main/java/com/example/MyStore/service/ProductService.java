package com.example.MyStore.service;

import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.service.ProductDetailsServiceModel;
import com.example.MyStore.model.service.ProductSummaryServiceModel;

import java.util.Collection;
import java.util.List;

public interface ProductService {
    List<ProductSummaryServiceModel> getAllProducts();

    List<ProductSummaryServiceModel> getAllProductsExceptOwn(String username);

    List<ProductSummaryServiceModel> getAllProductsExceptOwnPageable(int page, int pageSize, String username);

    List<ProductSummaryServiceModel> getAllProductsByCategoryExceptOwn(String username, String categoryName);

    List<ProductSummaryServiceModel> getAllProductsByCategoryExceptOwnPageable(int page, int pageSize, String username, String categoryName);


    ProductDetailsServiceModel getProductById(Long id);

    Integer getAvailableQuantityById(Long id);

    Product findById(Long id);
}
