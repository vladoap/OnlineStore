package com.example.MyStore.service.impl;

import com.example.MyStore.exception.ProductNotFoundException;
import com.example.MyStore.model.entity.Category;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.entity.User;
import com.example.MyStore.model.enums.CategoryNameEnum;
import com.example.MyStore.model.service.ProductAddServiceModel;
import com.example.MyStore.model.service.ProductDetailsServiceModel;
import com.example.MyStore.model.service.ProductUpdateServiceModel;
import com.example.MyStore.repository.ProductRepository;
import com.example.MyStore.service.CategoryService;
import com.example.MyStore.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    private ProductService productService;

    private ModelMapper modelMapper = new ModelMapper();
    @Mock

    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @Captor
    private ArgumentCaptor<Product> productArgumentCaptor;

    @BeforeEach
    public void init() {
        productService = new ProductServiceImpl(productRepository, categoryService, modelMapper);

    }

    @Test
    public void testGetByIdShouldReturnProductDetailsServiceModel() {
        Product productToTest = initProduct();

        when(productRepository.findById(productToTest.getId()))
                .thenReturn(Optional.of(productToTest));

        ProductDetailsServiceModel productServiceModel = productService.getById(productToTest.getId());

        assertEquals(productToTest.getId(), productServiceModel.getId());
        assertEquals(productToTest.getName(), productServiceModel.getName());
        assertEquals(productToTest.getDescription(), productServiceModel.getDescription());
        assertEquals(productToTest.getSeller(), productServiceModel.getSeller());
        assertEquals(productToTest.getCategory().getName(), productServiceModel.getCategory());
        assertEquals(productToTest.getPictures(), productServiceModel.getPictures());
        assertEquals(productToTest.getQuantity(), productServiceModel.getQuantity());
        assertEquals(productToTest.getPrice(), productServiceModel.getPrice());
    }

    @Test
    public void testGetByIdShouldThrowWhenNotFound() {
        assertThrows(ProductNotFoundException.class, () -> productService.getById(20L));

    }

    @Test
    public void testGetAvailableQuantityByIdShouldReturnQuantity() {
        Product productToTest = initProduct();

        when(productRepository.findById(productToTest.getId()))
                .thenReturn(Optional.of(productToTest));

        Integer availableQuantityById = productService.getAvailableQuantityById(productToTest.getId());

        assertEquals(productToTest.getQuantity(), availableQuantityById);
    }

    @Test
    public void testGetAvailableQuantityByIdShouldThrowWhenNotFound() {
        assertThrows(ProductNotFoundException.class, () -> productService.getAvailableQuantityById(20L));
    }

    @Test
    public void testFindByIdShouldReturnProductUpdateServiceModel() {
        Product productToTest = initProduct();

        when(productRepository.findById(productToTest.getId()))
                .thenReturn(Optional.of(productToTest));

        ProductUpdateServiceModel productServiceModel = productService.findById(productToTest.getId());

        assertEquals(productToTest.getId(), productServiceModel.getId());
        assertEquals(productToTest.getName(), productServiceModel.getName());
        assertEquals(productToTest.getDescription(), productServiceModel.getDescription());
        assertEquals(productToTest.getCategory().getName(), productServiceModel.getCategory());
        assertEquals(productToTest.getPictures(), productServiceModel.getPictures());
        assertEquals(productToTest.getQuantity(), productServiceModel.getQuantity());
        assertEquals(productToTest.getPrice(), productServiceModel.getPrice());
    }

    @Test
    public void testGetProductByIdShouldThrowWhenNotFound() {
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(20L));
    }


    @Test
    public void testGetProductByIdShouldReturnProduct() {
        Product productToTest = initProduct();

        when(productRepository.findById(productToTest.getId()))
                .thenReturn(Optional.of(productToTest));

        Product product = productService.getProductById(productToTest.getId());

        assertEquals(productToTest, product);
    }

    @Test
    public void testFindByIdShouldThrowWhenNotFound() {
        assertThrows(ProductNotFoundException.class, () -> productService.findById(20L));
    }


    @Test
    public void testUpdateProductWithAddAndDeletePicture() {
        Product productToTest = initProduct();

        ProductUpdateServiceModel productServiceModel = new ProductUpdateServiceModel()
                .setQuantity(200)
                .setDescription("Test Description")
                .setPictures(new ArrayList<>(productToTest.getPictures()))
                .setPrice(BigDecimal.valueOf(50.50))
                .setName("Updated name")
                .setCategory(CategoryNameEnum.Books)
                .setId(productToTest.getId());

        Picture pictureToAdd = new Picture()
                .setTitle("pictureToAdd");
        productServiceModel.getPictures().add(pictureToAdd);

        when(categoryService.findByName(productServiceModel.getCategory().name()))
                .thenReturn(productToTest.getCategory());

        when(productRepository.findById(productToTest.getId()))
                .thenReturn(Optional.of(productToTest));

        assertEquals(2, productToTest.getPictures().size());

        List<Picture> picturesForDelete = productService.updateProduct(productServiceModel);

        verify(productRepository).save(productArgumentCaptor.capture());

        Product actualSavedProduct = productArgumentCaptor.getValue();

        assertEquals(0, picturesForDelete.size());
        assertEquals(3, actualSavedProduct.getPictures().size());
        assertEquals(productToTest.getQuantity(), actualSavedProduct.getQuantity());
        assertEquals(productToTest.getDescription(), actualSavedProduct.getDescription());
        assertEquals(productToTest.getPictures(), actualSavedProduct.getPictures());
        assertEquals(productToTest.getPrice(), actualSavedProduct.getPrice());
        assertEquals(productToTest.getName(), actualSavedProduct.getName());
        assertEquals(productToTest.getCategory(), actualSavedProduct.getCategory());

        productServiceModel.setPictures(new ArrayList<>(productToTest.getPictures()));
        productServiceModel.getPictures().remove(2);

        picturesForDelete = productService.updateProduct(productServiceModel);

        assertEquals(1, picturesForDelete.size());
        assertEquals(pictureToAdd, picturesForDelete.get(0));
    }

    @Test
    public void testDeleteProductIsDeleted() {
        Product product = initProduct();

        when(productRepository.findById(product.getId()))
                .thenReturn(Optional.of(product));

        boolean isDeleted = productService.deleteProduct(product.getId());

        verify(productRepository).deleteById(product.getId());
        assertTrue(isDeleted);
    }

    @Test
    public void testDeleteProductIsNotDeleted() {
        Product product = initProduct();

        when(productRepository.findById(product.getId()))
                .thenReturn(Optional.empty());

        boolean isDeleted = productService.deleteProduct(product.getId());

        assertFalse(isDeleted);
    }

    @Test
    public void testCreateProduct() {
        User user = new User()
                .setUsername("admin")
                .setProducts(new HashSet<>());

        Category category = new Category()
                .setName(CategoryNameEnum.Home)
                .setDescription("description");

        ProductAddServiceModel productServiceModel = new ProductAddServiceModel()
                .setName("new_product")
                .setDescription("description of the new product")
                .setQuantity(25)
                .setPrice(BigDecimal.valueOf(9.00))
                .setCategory(CategoryNameEnum.Home);

        when(categoryService.findByName(productServiceModel.getCategory().name()))
                .thenReturn(category);

        productService.createProduct(productServiceModel, user);

        verify(productRepository).save(productArgumentCaptor.capture());

        Product actualSavedProduct = productArgumentCaptor.getValue();

        assertEquals(productServiceModel.getName(), actualSavedProduct.getName());
        assertEquals(productServiceModel.getDescription(), actualSavedProduct.getDescription());
        assertEquals(productServiceModel.getQuantity(), actualSavedProduct.getQuantity());
        assertEquals(productServiceModel.getPrice(), actualSavedProduct.getPrice());
        assertEquals(user, actualSavedProduct.getSeller());
        assertEquals(category, actualSavedProduct.getCategory());
        assertTrue(user.getProducts().contains(actualSavedProduct));
    }

    @Test
    public void testAddProductPicture() {
        Picture pictureToAdd = new Picture()
                .setTitle("new picture");

        Product product = initProduct();

        when(productRepository.findById(product.getId()))
                .thenReturn(Optional.of(product));

        productService.addProductPicture(product.getId(), pictureToAdd);

        verify(productRepository).save(productArgumentCaptor.capture());

        Product actualSavedProduct = productArgumentCaptor.getValue();

        assertTrue(actualSavedProduct.getPictures().contains(pictureToAdd));
        assertEquals(3, actualSavedProduct.getPictures().size());
    }

    @Test
    public void testReduceProductsQuantityByUser() {
        User user = UserServiceImplTest.initUser();

        Product product1 = new Product()
                .setQuantity(20);
        product1.setId(1L);

        Product product2 = new Product()
                .setQuantity(20);
        product2.setId(2L);

        Product product3 = new Product()
                .setQuantity(20);
        product3.setId(3L);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product1));

        when(productRepository.findById(2L))
                .thenReturn(Optional.of(product2));

        when(productRepository.findById(3L))
                .thenReturn(Optional.of(product3));

        productService.reduceProductsQuantityByUser(user);

        verify(productRepository, times(3)).save(productArgumentCaptor.capture());

        List<Product> actualSavedProducts = productArgumentCaptor.getAllValues();

        int expectedQuantity = 15;
        for (int i = 0; i < 3; i++) {
            assertEquals(expectedQuantity, actualSavedProducts.get(i).getQuantity());

            if (i == 0) {
                expectedQuantity = expectedQuantity - 5;
            } else if (i == 1) {
                expectedQuantity = 0;
            }
        }

    }


    private static Product initProduct() {
        Category category = new Category()
                .setName(CategoryNameEnum.Electronics)
                .setDescription("Category - Electronics!");

        Picture picture1 = new Picture()
                .setTitle("picture1");

        Picture picture2 = new Picture()
                .setTitle("picture2");

        User seller = new User()
                .setUsername("admin");

        Product product = new Product()
                .setName("TV")
                .setDescription("Sony TV")
                .setSeller(seller)
                .setCategory(category)
                .setPictures(new ArrayList<>(List.of(picture1, picture2)))
                .setQuantity(15)
                .setPrice(BigDecimal.valueOf(999.99));

        product.setId(2L);

        return product;
    }

}