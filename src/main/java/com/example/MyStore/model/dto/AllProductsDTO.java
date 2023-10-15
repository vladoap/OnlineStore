package com.example.MyStore.model.dto;

import com.example.MyStore.model.view.ProductsSummaryViewModel;

import java.util.List;

public class AllProductsDTO {

    private List<ProductsSummaryViewModel> products;
    private List<Integer> pages;
    private int clickedPage;


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

    public int getClickedPage() {
        return clickedPage;
    }

    public AllProductsDTO setClickedPage(int clickedPage) {
        this.clickedPage = clickedPage;
        return this;
    }


}
