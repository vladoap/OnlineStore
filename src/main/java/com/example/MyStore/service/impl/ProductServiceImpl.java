package com.example.MyStore.service.impl;

import com.example.MyStore.exception.ProductNotFoundException;
import com.example.MyStore.model.entity.CartItem;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.entity.User;
import com.example.MyStore.model.enums.CategoryNameEnum;
import com.example.MyStore.model.service.ProductAddServiceModel;
import com.example.MyStore.model.service.ProductUpdateServiceModel;
import com.example.MyStore.model.service.ProductDetailsServiceModel;
import com.example.MyStore.model.service.ProductSummaryServiceModel;
import com.example.MyStore.repository.ProductRepository;
import com.example.MyStore.service.CategoryService;
import com.example.MyStore.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;


    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
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
    public List<ProductSummaryServiceModel> getAllProductsPageable(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        return productRepository.findAll(pageRequest)
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
    public ProductDetailsServiceModel getById(Long id) {

        return productRepository
                .findById(id)
                .map(product -> {
                    ProductDetailsServiceModel mappedProduct = modelMapper.map(product, ProductDetailsServiceModel.class);
                    mappedProduct.setCategory(product.getCategory().getName());

                    return mappedProduct;
                })
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
    public ProductUpdateServiceModel findById(Long id) {
        return productRepository
                .findById(id)
                .map(product ->  {
                    ProductUpdateServiceModel mappedProduct = modelMapper.map(product, ProductUpdateServiceModel.class);
                    mappedProduct.setCategory(product.getCategory().getName());

                    return mappedProduct;
                })
                .orElseThrow(() -> new ProductNotFoundException("Product with ID: " + id + " not found."));
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID: " + id + " not found."));
    }

    @Override
    public List<Picture> updateProduct(ProductUpdateServiceModel productUpdateServiceModel) {
        Product product = getProductById(productUpdateServiceModel.getId());
        List<Picture> deletedPictures = new ArrayList<>();

        product.getPictures().stream()
                .filter(oldPic -> !productUpdateServiceModel.getPictures().contains(oldPic))
                .forEach(deletedPictures::add);


        product
                .setQuantity(productUpdateServiceModel.getQuantity())
                .setDescription(productUpdateServiceModel.getDescription())
                .setPictures(productUpdateServiceModel.getPictures())
                .setPrice(productUpdateServiceModel.getPrice())
                .setName(productUpdateServiceModel.getName())
                .setCategory(categoryService.findByName(productUpdateServiceModel.getCategory().name()));


        productRepository.save(product);
        return deletedPictures;
    }

    @Override
    public boolean deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }


    @Override
    public void createProduct(ProductAddServiceModel productServiceModel, User user) {

        Product product = modelMapper.map(productServiceModel, Product.class);
        product
                .setSeller(user)
                .setPictures(new ArrayList<>())
                .setCategory(categoryService.findByName(productServiceModel.getCategory().name()));
        product.setCreated(LocalDateTime.now());

        productRepository.save(product);

        user.getProducts().add(product);

    }

    @Override
    public void addProductPicture(Long productId, Picture picture) {
        Product product = getProductById(productId);
        product.getPictures().add(picture);

        productRepository.save(product);
    }

    @Override
    public void reduceProductsQuantityByUser(User buyer) {


        Set<CartItem> boughtProducts = buyer.getCart().getCartItems();

        boughtProducts.stream()
                .collect(Collectors.groupingBy(CartItem::getProductId, Collectors.summingInt(CartItem::getQuantity)))
                .forEach((productId, totalQuantity) -> {
                    Product product = productRepository.findById(productId).orElse(null);
                    if (product != null) {
                        int newQuantity = product.getQuantity() - totalQuantity;
                        if (newQuantity >= 0) {
                            product.setQuantity(newQuantity);
                        } else {
                            product.setQuantity(0);
                        }
                        productRepository.save(product);
                    } else {
                        throw new ProductNotFoundException("Product with ID: " + productId + " not found.");
                    }
                });
    }



    private List<Product> mapCartItemsToProducts(Set<CartItem> boughtProducts) {
        return boughtProducts
                .stream()
                .map(cartItem -> productRepository.findById(cartItem.getId())
                        .orElseThrow(() -> new ProductNotFoundException("Product with ID: " + cartItem.getProductId() + " not found.")))
                .collect(Collectors.toList());

    }


    private ProductSummaryServiceModel map(Product product) {
        ProductSummaryServiceModel productServiceModel = modelMapper.map(product, ProductSummaryServiceModel.class);

        Optional<Picture> picture = product.getPictures().stream().findFirst();

        picture.ifPresent(pic -> productServiceModel.setImageUrl(picture.get().getUrl()));

        return productServiceModel;
    }

    public Boolean isOwner(String username, Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);


        if (productOpt.isEmpty()) {
            return false;
        }

        return productOpt.get().getSeller().getUsername().equalsIgnoreCase(username);
    }


    public Boolean isNotOwner(String username, Long productId) {
        return !isOwner(username, productId);
    }
}
