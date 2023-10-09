package com.example.MyStore.service;

import com.example.MyStore.model.service.ProductSummaryServiceModel;

import java.util.List;

public interface ProductService {
    List<ProductSummaryServiceModel> getAllProducts();
}
