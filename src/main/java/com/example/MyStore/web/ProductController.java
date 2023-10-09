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
import java.util.stream.Collectors;

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

        List<ProductSummaryServiceModel> allProductsExceptOwnPageable = productService.getAllProductsExceptOwnPageable(page, pageSize, principal.getName());

        List<ProductsSummaryViewModel> products = allProductsExceptOwnPageable.stream().map(p -> modelMapper.map(p, ProductsSummaryViewModel.class)).toList();;

        int totalProducts = productService.getAllProductsExceptOwn(principal.getName()).size();

        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        List<Integer> pages = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pages.add(i);
        }

        model.addAttribute("products", products);
        model.addAttribute("pages", pages);

        model.addAttribute("clickedPage", clickedPage);

        return "shop";
    }


    @GetMapping("/details/{id}")
    public String productDetails(@PathVariable Long id) {

        return "shop-single";
    }




    //    @GetMapping("/all")
//    public String getAllProducts(Model model, Principal principal) {
//
//        model.addAttribute("products", productService.getAllProductsExceptOwn(principal.getName()));
//
//        return "shop";
//    }

}