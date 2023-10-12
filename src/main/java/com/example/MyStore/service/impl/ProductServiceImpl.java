package com.example.MyStore.service.impl;

import com.example.MyStore.exception.ProductNotFoundException;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.enums.CategoryNameEnum;
import com.example.MyStore.model.service.ProductAddServiceModel;
import com.example.MyStore.model.service.ProductDetailsServiceModel;
import com.example.MyStore.model.service.ProductSummaryServiceModel;
import com.example.MyStore.repository.ProductRepository;
import com.example.MyStore.service.ProductService;
import com.example.MyStore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;


    public ProductServiceImpl(ProductRepository productRepository, UserService userService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProductSummaryServiceModel> getAllProducts() {
        return productRepository
                .findAll()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductSummaryServiceModel> getAllProductsExceptOwn(String username) {
        return productRepository
                .findAllBySellerUsernameIsNot(username)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductSummaryServiceModel> getAllProductsExceptOwnPageable(int page, int pageSize, String username) {

        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        return productRepository.findAllBySellerUsernameIsNotByPagination(username, pageRequest)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductSummaryServiceModel> getAllProductsByCategoryExceptOwnPageable(int page, int pageSize, String username, String categoryName) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        return productRepository.findAllByCategoryAndSellerUsernameIsNotByPagination(username, CategoryNameEnum.valueOf(categoryName), pageRequest)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }


    @Override
    public List<ProductSummaryServiceModel> getAllProductsByCategoryExceptOwn(String username, String categoryName) {
        return productRepository
                .findAllByCategoryNameAndSellerUsernameIsNot(CategoryNameEnum.valueOf(categoryName), username)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductSummaryServiceModel> getAllProductsForUserPageable(int page, int pageSize, String username) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        return productRepository.findAllBySellerUsernameByPagination(username, pageRequest)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductSummaryServiceModel> getAllProductsForUser(String username) {
        return productRepository
                .findAllBySellerUsername(username)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }



    @Override
    public ProductDetailsServiceModel getProductById(Long id) {

        return productRepository
                .findById(id)
                .map(product -> modelMapper.map(product, ProductDetailsServiceModel.class))
                .orElseThrow(() -> new ProductNotFoundException("Product with ID: " + id + " not found."));
    }

    @Override
    public Integer getAvailableQuantityById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID: " + id + " not found."))
                .getQuantity();
    }




    @Override
    public List<ProductSummaryServiceModel> getTheLatestThreeProducts() {
        Pageable pageable = PageRequest.of(0, 3, Sort.by("created"));

        return productRepository
                .findAll(pageable)
                .map(this::map)
                .toList();
    }

    @Override
    public ProductAddServiceModel findProductById(Long id) {
        return productRepository
                .findById(id)
                .map(product -> modelMapper.map(product, ProductAddServiceModel.class))
                .orElseThrow(() -> new ProductNotFoundException("Product with ID: " + id + " not found."));
    }


    private ProductSummaryServiceModel map(Product product) {
        ProductSummaryServiceModel productServiceModel = modelMapper.map(product, ProductSummaryServiceModel.class);

        Optional<Picture> picture = product.getPictures().stream().findFirst();

        picture.ifPresent(pic -> productServiceModel.setImageUrl(picture.get().getUrl()));

        return productServiceModel;
    }

    public Boolean isOwner(String username, Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);


        if (productOpt.isEmpty() || (userService.findByUsername(username) == null)) {
            return false;
        }

        return productOpt.get().getSeller().getUsername().equalsIgnoreCase(username);
    }


    public Boolean isNotOwner(String username, Long productId) {
        return !isOwner(username, productId);
    }
}
