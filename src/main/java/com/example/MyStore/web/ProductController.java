package com.example.MyStore.web;

import com.example.MyStore.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public String shop(Model model) {

//        model.addAttribute("products", productService.getAllProducts());

        return "shop";
    }

    @GetMapping("/single")
    public String shopSingle() {
        return "shop-single";
    }
}