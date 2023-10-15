package com.example.MyStore.model.dto;

import com.example.MyStore.model.view.ProductsSummaryViewModel;

import java.util.List;

public class AllProductsDTO {

    private List<ProductsSummaryViewModel> products;
    private List<Integer> pages;



    public List<ProductsSummaryViewModel> getProducts() {
        return products;
    }

    public AllProductsDTO setProducts(List<ProductsSummaryViewModel> products) {
        this.products = products;
        return this;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public AllProductsDTO setPages(List<Integer> pages) {
        this.pages = pages;
        return this;
    }




}
