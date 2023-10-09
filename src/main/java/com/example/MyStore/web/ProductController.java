package com.example.MyStore.web;

import com.example.MyStore.model.service.ProductSummaryServiceModel;
import com.example.MyStore.model.view.ProductsSummaryViewModel;
import com.example.MyStore.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
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

        System.out.println("WORKINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
        return "shop";
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
    public String productDetails(@PathVariable Long id) {

        return "shop-single";
    }

    private static List<Integer> getPageCount(int pageSize, int totalProducts) {
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        List<Integer> pages = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pages.add(i);
        }
        return pages;
    }

    private List<ProductsSummaryViewModel> mapServiceToViewModel(List<ProductSummaryServiceModel> productsPageable) {
        return productsPageable
                .stream()
                .map(p -> modelMapper.map(p, ProductsSummaryViewModel.class))
                .toList();
    }







}