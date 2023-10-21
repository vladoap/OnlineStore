package com.example.MyStore.web;

import com.example.MyStore.model.service.ProductSummaryServiceModel;
import com.example.MyStore.model.view.ProductLatestViewModel;
import com.example.MyStore.service.PictureService;
import com.example.MyStore.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;
    private final PictureService pictureService;
    private final ModelMapper modelMapper;

    public HomeController(ProductService productService, PictureService pictureService, ModelMapper modelMapper) {
        this.productService = productService;
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public String index(Principal principal) {
        if (principal != null) {
            return "redirect:home";
        }
        return "index";
    }

    @GetMapping("home")
    public String home(Model model) {
        List<ProductSummaryServiceModel> productsServiceModel = productService.getTheLatestThreeProducts();

        List<ProductLatestViewModel> productsViewModel = productsServiceModel
                .stream()
                .map(product ->  {
                    ProductLatestViewModel mappedProduct = modelMapper.map(product, ProductLatestViewModel.class);
                    if (mappedProduct.getImageUrl() == null) {
                        mappedProduct.setImageUrl(pictureService.getDefaultProductPicture().getUrl());
                    }

                    return mappedProduct;
                })
                .toList();

         model.addAttribute("latestProducts", productsViewModel);

        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }


}
