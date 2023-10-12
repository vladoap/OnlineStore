package com.example.MyStore.web;

import com.example.MyStore.model.binding.ProductPurchaseBindingModel;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.service.ProductDetailsServiceModel;
import com.example.MyStore.model.service.ProductSummaryServiceModel;
import com.example.MyStore.model.view.ProductDetailsViewModel;
import com.example.MyStore.model.view.ProductsSummaryViewModel;
import com.example.MyStore.service.PictureService;
import com.example.MyStore.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final PictureService pictureService;
    private final ModelMapper modelMapper;

    public ProductController(ProductService productService, PictureService pictureService, ModelMapper modelMapper) {
        this.productService = productService;
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute
    public ProductPurchaseBindingModel productPurchaseBindingModel() {
        return new ProductPurchaseBindingModel();
    }

    @GetMapping("/all")
    public String getAllProducts(Model model, Principal principal,
                                 @RequestParam(name = "clickedPage", required = false, defaultValue = "0") Integer clickedPage,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "6") int pageSize) {

        List<ProductSummaryServiceModel> productsPageable = productService.getAllProductsExceptOwnPageable(page, pageSize, principal.getName());

        int totalProducts = productService.getAllProductsExceptOwn(principal.getName()).size();

        model.addAttribute("products", mapServiceToViewModel(productsPageable));
        model.addAttribute("pages", getPageCount(pageSize, totalProducts));
        model.addAttribute("clickedPage", clickedPage);
        model.addAttribute("selectedCategoryName", null);

        return "shop";
    }

    @GetMapping("/own")
    public String getAllOwnProducts(Model model, Principal principal,
                                    @RequestParam(name = "clickedPage", required = false, defaultValue = "0") Integer clickedPage,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "6") int pageSize) {

       List<ProductSummaryServiceModel> productsPageable = productService.getAllProductsForUserPageable(page, pageSize, principal.getName());

       int totalProducts = productService.getAllProductsForUser(principal.getName()).size();

        model.addAttribute("products", mapServiceToViewModel(productsPageable));
        model.addAttribute("pages", getPageCount(pageSize, totalProducts));
        model.addAttribute("clickedPage", clickedPage);

        return "my-products";
    }


    @GetMapping("/category/{name}")
    public String getAllProductsByCategory(Model model, Principal principal,
                                           @PathVariable(name = "name") String categoryName,
                                           @RequestParam(name = "clickedPage", required = false, defaultValue = "0") Integer clickedPage,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "6") int pageSize) {

        List<ProductSummaryServiceModel> productsPageable = productService.getAllProductsByCategoryExceptOwnPageable(page, pageSize, principal.getName(), categoryName);

        int totalProducts = productService.getAllProductsByCategoryExceptOwn(principal.getName(), categoryName).size();

        model.addAttribute("products", mapServiceToViewModel(productsPageable));
        model.addAttribute("pages", getPageCount(pageSize, totalProducts));
        model.addAttribute("clickedPage", clickedPage);
        model.addAttribute("selectedCategoryName", categoryName);



        return "shop";
    }

    @GetMapping("/details/{id}")
    public String productDetails(@PathVariable Long id, Model model) {

        ProductDetailsServiceModel productDetailsServiceModel = productService.getProductById(id);

        ProductDetailsViewModel productDetailsViewModel = mapServiceToViewModel(productDetailsServiceModel);

        List<List<Picture>> groupedPictures = new ArrayList<>();
        List<Picture> pictures = productDetailsViewModel.getPictures();
        int batchSize = 3;

        for (int i = 0; i < pictures.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, pictures.size());
            groupedPictures.add(pictures.subList(i, endIndex));
        }

        model.addAttribute("productDetails", productDetailsViewModel);
        model.addAttribute("groupedPictures", groupedPictures);


        return "shop-single";
    }

//    @GetMapping("/edit/{id}")
//    public String productEditGet(@PathVariable Long id, Model model) {
//
//
//        return "product-edit";
//    }



    private static List<Integer> getPageCount(int pageSize, int totalProducts) {
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        List<Integer> pages = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pages.add(i);
        }
        return pages;
    }

    private List<ProductsSummaryViewModel> mapServiceToViewModel(List<ProductSummaryServiceModel> productsServiceModel) {
        return productsServiceModel
                .stream()
                .map(p -> {
                    ProductsSummaryViewModel mappedProduct = modelMapper.map(p, ProductsSummaryViewModel.class);
                    if (mappedProduct.getImageUrl() == null) {
                        mappedProduct.setImageUrl(pictureService.getDefaultPicture().getUrl());
                    }

                    return mappedProduct;
                })
                .toList();
    }

    private ProductDetailsViewModel mapServiceToViewModel(ProductDetailsServiceModel productServiceModel) {
        return new ProductDetailsViewModel()
                .setCategory(productServiceModel.getCategory().getName().name())
                .setId(productServiceModel.getId())
                .setDescription(productServiceModel.getDescription())
                .setName(productServiceModel.getName())
                .setPictures(productServiceModel.getPictures())
                .setImageUrl(productServiceModel
                        .getPictures()
                        .stream()
                        .findFirst().map(Picture::getUrl)
                        .orElse(pictureService.getDefaultPicture().getUrl()))
                .setPrice(productServiceModel.getPrice())
                .setQuantity(productServiceModel.getQuantity())
                .setSeller(productServiceModel.getSeller().getFirstName() + " " + productServiceModel.getSeller().getLastName());

    }


}