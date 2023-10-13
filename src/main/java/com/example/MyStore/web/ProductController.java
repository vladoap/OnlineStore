package com.example.MyStore.web;

import com.example.MyStore.model.binding.ProductAddBindingModel;
import com.example.MyStore.model.binding.ProductUpdateBindingModel;
import com.example.MyStore.model.binding.ProductPurchaseBindingModel;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.enums.CategoryNameEnum;
import com.example.MyStore.model.service.ProductAddServiceModel;
import com.example.MyStore.model.service.ProductUpdateServiceModel;
import com.example.MyStore.model.service.ProductDetailsServiceModel;
import com.example.MyStore.model.service.ProductSummaryServiceModel;
import com.example.MyStore.model.view.ProductDetailsViewModel;
import com.example.MyStore.model.view.ProductsSummaryViewModel;
import com.example.MyStore.service.CategoryService;
import com.example.MyStore.service.PictureService;
import com.example.MyStore.service.ProductService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final PictureService pictureService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public ProductController(ProductService productService, PictureService pictureService, CategoryService categoryService, ModelMapper modelMapper) {
        this.productService = productService;
        this.pictureService = pictureService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute
    public ProductPurchaseBindingModel productPurchaseBindingModel() {
        return new ProductPurchaseBindingModel();
    }

    @ModelAttribute
    public ProductAddBindingModel productAddBindingModel() {
        return new ProductAddBindingModel();
    }

    @GetMapping("/all")
    public String getAllProducts(Model model, Principal principal,
                                 @RequestParam(name = "clickedPage", required = false, defaultValue = "0") Integer clickedPage,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "6") int pageSize) {

        List<ProductSummaryServiceModel> productsPageable = productService.getAllProductsExceptOwnPageable(page, pageSize, principal.getName());

        int totalProducts = productService.getAllProductsExceptOwn(principal.getName()).size();

        model.addAttribute("products", mapServiceToDetailsViewModel(productsPageable));
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

        if (totalProducts == 0) {
            model.addAttribute("noProducts", true);
        }

        model.addAttribute("products", mapServiceToDetailsViewModel(productsPageable));
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

        model.addAttribute("products", mapServiceToDetailsViewModel(productsPageable));
        model.addAttribute("pages", getPageCount(pageSize, totalProducts));
        model.addAttribute("clickedPage", clickedPage);
        model.addAttribute("selectedCategoryName", categoryName);


        return "shop";
    }

    @GetMapping("/details/{id}")
    public String productDetails(@PathVariable Long id, Model model) {

        ProductDetailsServiceModel productDetailsServiceModel = productService.getById(id);

        ProductDetailsViewModel productDetailsViewModel = mapServiceToDetailsViewModel(productDetailsServiceModel);

        List<List<String>> groupedPictures = new ArrayList<>();
        List<String> pictures = productDetailsViewModel.getPictures();
        int batchSize = 3;

        for (int i = 0; i < pictures.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, pictures.size());
            groupedPictures.add(pictures.subList(i, endIndex));
        }

        model.addAttribute("productDetails", productDetailsViewModel);
        model.addAttribute("groupedPictures", groupedPictures);


        return "shop-single";
    }

    @PreAuthorize("@productServiceImpl.isOwner(#principal.name, #id)")
    @GetMapping("/update/{id}")
    public String productUpdateGet(@PathVariable Long id, Model model, Principal principal) {

        if (!model.containsAttribute("productModel")) {
            ProductUpdateServiceModel productUpdateServiceModel = productService.findById(id);
            ProductUpdateBindingModel productUpdateBindingModel = mapServiceToAddBindingModel(productUpdateServiceModel);
            model.addAttribute("productModel", productUpdateBindingModel);
        }

        model.addAttribute("categories", CategoryNameEnum.values());

        return "product-update";
    }


    @PreAuthorize("@productServiceImpl.isOwner(#principal.name, #id)")
    @PatchMapping("/update/{id}")
    public String productUpdatePost(@PathVariable Long id, Principal principal, @Valid ProductUpdateBindingModel productModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("productModel", productModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.productModel", bindingResult);
            return "redirect:/products/update/" + id;
        }


        ProductUpdateServiceModel productUpdateServiceModel = mapAddBindingToServiceModel(productModel);

        List<Picture> deletedPictures = productService.updateProduct(productUpdateServiceModel);
        pictureService.deletePictures(deletedPictures);


        return "redirect:/products/own";
    }

    @GetMapping("/add")
    public String productAdd(Model model) {

        if (!model.containsAttribute("productModel")) {

            model.addAttribute("productModel", productAddBindingModel());
        }
        model.addAttribute("categories", CategoryNameEnum.values());
        return "product-add";
    }

    @PostMapping("/add")
    public String productAddPost(@Valid ProductAddBindingModel productModel, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                 Principal principal) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("productModel", productModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.productModel", bindingResult);
            return "redirect:add";
        }

        ProductAddServiceModel productServiceModel = modelMapper.map(productModel, ProductAddServiceModel.class);
        productServiceModel.setCategory(categoryService.findByName(productModel.getCategory()));

        productService.addProduct(productServiceModel, principal.getName());

        return "redirect:own";
    }

    @PreAuthorize("@productServiceImpl.isOwner(#principal.name, #id)")
    @DeleteMapping("/delete/{id}")
    public String productDelete(@PathVariable Long id, Principal principal) {
        productService.deleteProduct(id);

        return "redirect:/products/own";
    }


    private static List<Integer> getPageCount(int pageSize, int totalProducts) {
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        List<Integer> pages = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pages.add(i);
        }
        return pages;
    }

    private List<ProductsSummaryViewModel> mapServiceToDetailsViewModel(List<ProductSummaryServiceModel> productsServiceModel) {
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

    private ProductDetailsViewModel mapServiceToDetailsViewModel(ProductDetailsServiceModel productServiceModel) {
        return new ProductDetailsViewModel()
                .setCategory(productServiceModel.getCategory().getName().name())
                .setId(productServiceModel.getId())
                .setDescription(productServiceModel.getDescription())
                .setName(productServiceModel.getName())
                .setPictures(productServiceModel.getPictures().stream().map(Picture::getUrl).collect(Collectors.toList()))
                .setImageUrl(productServiceModel
                        .getPictures()
                        .stream()
                        .findFirst().map(Picture::getUrl)
                        .orElse(pictureService.getDefaultPicture().getUrl()))
                .setPrice(productServiceModel.getPrice())
                .setQuantity(productServiceModel.getQuantity())
                .setSeller(productServiceModel.getSeller().getFirstName() + " " + productServiceModel.getSeller().getLastName());

    }


    private ProductUpdateBindingModel mapServiceToAddBindingModel(ProductUpdateServiceModel productUpdateServiceModel) {
        return new ProductUpdateBindingModel()
                .setCategory(productUpdateServiceModel.getCategory().getName().name())
                .setId(productUpdateServiceModel.getId())
                .setDescription(productUpdateServiceModel.getDescription())
                .setName(productUpdateServiceModel.getName())
                .setPictures(productUpdateServiceModel.getPictures().stream().map(Picture::getUrl).collect(Collectors.toList()))
                .setPrice(productUpdateServiceModel.getPrice())
                .setQuantity(productUpdateServiceModel.getQuantity());

    }

    private ProductUpdateServiceModel mapAddBindingToServiceModel(ProductUpdateBindingModel productUpdateBindingModel) {
        ProductUpdateServiceModel productModel = modelMapper.map(productUpdateBindingModel, ProductUpdateServiceModel.class);

        List<Picture> pictures = productUpdateBindingModel.getPictures().stream()
                .map(pictureService::findByUrl).toList();

        productModel
                .setPictures(pictures)
                .setCategory(categoryService.findByName(productUpdateBindingModel.getCategory()));

        return productModel;
    }

}