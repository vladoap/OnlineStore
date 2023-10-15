package com.example.MyStore.web.rest;

import com.example.MyStore.model.dto.AllProductsDTO;
import com.example.MyStore.model.service.ProductSummaryServiceModel;
import com.example.MyStore.model.view.ProductsSummaryViewModel;
import com.example.MyStore.service.PictureService;
import com.example.MyStore.service.ProductService;
import com.example.MyStore.utils.PaginationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private final ProductService productService;
    private final PictureService pictureService;
    private final ModelMapper modelMapper;

    public AdminRestController(ProductService productService, ModelMapper modelMapper, PictureService pictureService) {
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.pictureService = pictureService;
    }

    @GetMapping("/products")
    public ResponseEntity<AllProductsDTO> getAllOffers(
            @RequestParam(name = "clickedPage", required = false, defaultValue = "0") Integer clickedPage,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int pageSize) {

        List<ProductSummaryServiceModel> productsPageable = productService.getAllProductsPageable(page, pageSize);

        int totalProducts = productService.getAllProducts().size();

        AllProductsDTO allProducts = new AllProductsDTO();
        allProducts
                .setProducts(mapServiceToDetailsViewModel(productsPageable))
                .setPages(PaginationUtil.getPageCount(pageSize, totalProducts))
                .setClickedPage(clickedPage);

        return ResponseEntity.ok(allProducts);
    }

    @DeleteMapping("/products/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {

        boolean isDeleted =  productService.deleteProduct(id);

        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();

    }


    private List<ProductsSummaryViewModel> mapServiceToDetailsViewModel(List<ProductSummaryServiceModel> productsServiceModel) {
        return productsServiceModel
                .stream()
                .map(p -> {
                    ProductsSummaryViewModel mappedProduct = modelMapper.map(p, ProductsSummaryViewModel.class);
                    if (mappedProduct.getImageUrl() == null) {
                        mappedProduct.setImageUrl(pictureService.getDefaultProductPicture().getUrl());
                    }

                    return mappedProduct;
                })
                .toList();
    }



}
