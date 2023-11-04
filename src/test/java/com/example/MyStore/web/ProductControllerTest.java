package com.example.MyStore.web;

import com.example.MyStore.helper.TestDataHelper;
import com.example.MyStore.model.binding.ProductAddBindingModel;
import com.example.MyStore.model.binding.ProductUpdateBindingModel;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.entity.User;
import com.example.MyStore.model.enums.CategoryNameEnum;
import com.example.MyStore.model.view.ProductDetailsViewModel;
import com.example.MyStore.model.view.ProductsSummaryViewModel;
import com.example.MyStore.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WithMockUser("admin")
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private TestDataHelper testDataHelper;

    private User testUser;


    @BeforeEach
    public void setUp() {
        testUser = testDataHelper.getUser1();
        testDataHelper.initProducts();
    }


    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        pictureRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        userRoleRepository.deleteAll();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllProductsExceptOwn() throws Exception {

        Product notOwnProduct = productRepository.findAll()
                .stream()
                .filter(pr -> !pr.getSeller().equals(testUser))
                .findFirst()
                .get();

        ResultActions resultActions = mockMvc
                .perform(get("/products/all")
                        .param("clickedPage", String.valueOf(2))
                        .param("page", String.valueOf(1))
                        .param("pageSize", String.valueOf(6))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("shop"))
                .andExpect(model().attribute("pages", List.of(1)))
                .andExpect(model().attribute("clickedPage", 2))
                .andExpect(model().attribute("selectedCategoryName", nullValue()))
                .andExpect(model().attributeExists("products"));

        List<ProductsSummaryViewModel> products = (List<ProductsSummaryViewModel>) resultActions.andReturn().getModelAndView()
                .getModel().get("products");

        ProductsSummaryViewModel productModel = products.get(0);

        assertEquals(notOwnProduct.getName(), productModel.getName());
        assertEquals(testDataHelper.getDefaultProductPicture().getUrl(), productModel.getImageUrl());
        assertEquals(notOwnProduct.getQuantity(), productModel.getQuantity());
        assertEquals(notOwnProduct.getPrice(), productModel.getPrice());
        assertNotEquals(notOwnProduct.getSeller(), testUser);
    }


    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllOwnProducts() throws Exception {
        Product ownProduct = productRepository.findAll()
                .stream()
                .filter(pr -> pr.getSeller().equals(testUser))
                .findFirst()
                .get();

        ResultActions resultActions = mockMvc
                .perform(get("/products/own")
                        .param("clickedPage", String.valueOf(2))
                        .param("page", String.valueOf(1))
                        .param("pageSize", String.valueOf(6))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("my-products"))
                .andExpect(model().attribute("pages", List.of(1)))
                .andExpect(model().attribute("clickedPage", 2));

        List<ProductsSummaryViewModel> ownProducts = (List<ProductsSummaryViewModel>) resultActions.andReturn().getModelAndView()
                .getModel().get("products");

        ProductsSummaryViewModel productModel = ownProducts.get(0);

        assertEquals(ownProduct.getName(), productModel.getName());
        assertEquals(ownProduct.getPictures().stream().findFirst().get().getUrl(), productModel.getImageUrl());
        assertEquals(ownProduct.getQuantity(), productModel.getQuantity());
        assertEquals(ownProduct.getPrice(), productModel.getPrice());
        assertEquals(ownProduct.getSeller(), testUser);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllOwnProductsWhenEmpty() throws Exception {
        Product ownProduct = productRepository.findAll()
                .stream()
                .filter(pr -> pr.getSeller().equals(testUser))
                .findFirst()
                .get();

        ownProduct.setSeller(userRepository.findByUsername("gosho").get());
        productRepository.save(ownProduct);

        ResultActions resultActions = mockMvc
                .perform(get("/products/own")
                        .param("clickedPage", String.valueOf(2))
                        .param("page", String.valueOf(1))
                        .param("pageSize", String.valueOf(6))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("my-products"))
                .andExpect(model().attribute("noProducts", true));

        List<ProductsSummaryViewModel> ownProducts = (List<ProductsSummaryViewModel>) resultActions.andReturn().getModelAndView()
                .getModel().get("products");

        assertTrue(ownProducts.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllProductsByCategory() throws Exception {
        Product notOwnProduct = productRepository.findAll()
                .stream()
                .filter(pr -> !pr.getSeller().equals(testUser))
                .findFirst()
                .get();

        String categoryToTest = notOwnProduct.getCategory().getName().name();

        ResultActions resultActions = mockMvc
                .perform(get("/products/category/" + categoryToTest)
                        .param("clickedPage", String.valueOf(2))
                        .param("page", String.valueOf(1))
                        .param("pageSize", String.valueOf(6))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("shop"))
                .andExpect(model().attribute("pages", List.of(1)))
                .andExpect(model().attribute("clickedPage", 2))
                .andExpect(model().attribute("selectedCategoryName", categoryToTest));

        List<ProductsSummaryViewModel> products = (List<ProductsSummaryViewModel>) resultActions.andReturn().getModelAndView()
                .getModel().get("products");

        ProductsSummaryViewModel productModel = products.get(0);

        assertEquals(1, products.size());
        assertEquals(notOwnProduct.getName(), productModel.getName());
        assertEquals(testDataHelper.getDefaultProductPicture().getUrl(), productModel.getImageUrl());
        assertEquals(notOwnProduct.getQuantity(), productModel.getQuantity());
        assertEquals(notOwnProduct.getPrice(), productModel.getPrice());
        assertNotEquals(notOwnProduct.getSeller(), testUser);
        assertEquals(notOwnProduct.getId(), productModel.getId());
    }

    @Test
    public void testProductDetails() throws Exception {
        Product productToTest = getTestProduct();


        ResultActions resultActions = mockMvc
                .perform(get("/products/details/" + productToTest.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("shop-single"));

        ProductDetailsViewModel product = (ProductDetailsViewModel) resultActions.andReturn().getModelAndView()
                .getModel().get("productDetails");

        assertEquals(productToTest.getId(), product.getId());
        assertEquals(productToTest.getName(), product.getName());
        assertEquals(productToTest.getPictures().stream().findFirst().get().getUrl(), product.getImageUrl());
        assertEquals(productToTest.getDescription(), product.getDescription());
        assertEquals(productToTest.getSeller().getFirstName() + " " + productToTest.getSeller().getLastName(), product.getSeller());
        assertEquals(productToTest.getCategory().getName(), product.getCategory());
        assertEquals(productToTest.getPictures().stream().map(Picture::getUrl).collect(Collectors.toList()), product.getPictures());
        assertEquals(productToTest.getQuantity(), product.getQuantity());
        assertEquals(productToTest.getPrice(), product.getPrice());
    }


    @Test
    public void testProductUpdateOpenUpdateForm() throws Exception {
        Product productToTest = getTestProduct();

        ResultActions resultActions = mockMvc
                .perform(get("/products/update/" + productToTest.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("product-update"))
                .andExpect(model().attributeExists("productModel"))
                .andExpect(model().attributeExists("categories"));

        ProductUpdateBindingModel productModel = (ProductUpdateBindingModel) resultActions.andReturn().getModelAndView().getModel().get("productModel");

        assertEquals(productToTest.getId(), productModel.getId());
        assertEquals(productToTest.getName(), productModel.getName());
        assertEquals(productToTest.getDescription(), productModel.getDescription());
        assertEquals(productToTest.getCategory().getName(), productModel.getCategory());
        assertEquals(productToTest.getPictures().stream().map(Picture::getUrl).collect(Collectors.toList()), productModel.getPictures());
        assertEquals(productToTest.getQuantity(), productModel.getQuantity());
        assertEquals(productToTest.getPrice(), productModel.getPrice());
    }

    @Test
    public void testProductUpdatePostWithValidationErrors() throws Exception {
        String invalidDescription = "d";

        Product productToTest = getTestProduct();

        mockMvc
                .perform(patch("/products/update/" + productToTest.getId())
                        .param("id", String.valueOf(productToTest.getId()))
                        .param("name", productToTest.getName())
                        .param("description", invalidDescription)
                        .param("category", productToTest.getCategory().getName().name())
                        .param("quantity", String.valueOf(productToTest.getQuantity()))
                        .param("price", productToTest.getPrice().toString())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/update/" + productToTest.getId()))
                .andExpect(flash().attributeExists("productModel"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.productModel"));
    }


    @Test
    public void testProductUpdatePost() throws Exception {
        String newDescription = "New description for product";
        String newName = "new name";
        CategoryNameEnum newCategory = CategoryNameEnum.Electronics;
        Integer newQuantity = 100;
        BigDecimal newPrice = BigDecimal.valueOf(12.35);

        Product productToTest = testDataHelper.initNewProductForUser1();
        Picture newPicture = new Picture()
                .setTitle("TestTitle")
                .setUrl("TestUrl")
                .setPublicId("testPublicId");
        newPicture
                .setCreated(LocalDateTime.now())
                .setId(30L);
        productToTest.setPictures(new ArrayList<>(List.of(newPicture)));
        productRepository.save(productToTest);

        String emptyString = "";

        assertEquals(1, productToTest.getPictures().size());

        mockMvc
                .perform(patch("/products/update/" + productToTest.getId())
                        .param("id", String.valueOf(productToTest.getId()))
                        .param("name", newName)
                        .param("description", newDescription)
                        .param("category", newCategory.name())
                        .param("quantity", newQuantity.toString())
                        .param("price", newPrice.toString())
                        .param("pictures", emptyString)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/own"));

        Product updatedProduct = productRepository.findById(productToTest.getId()).get();

        assertEquals(newName, updatedProduct.getName());
        assertEquals(newDescription, updatedProduct.getDescription());
        assertEquals(newCategory, updatedProduct.getCategory().getName());
        assertEquals(newQuantity, updatedProduct.getQuantity());
        assertEquals(newPrice, updatedProduct.getPrice());
        assertEquals(0, updatedProduct.getPictures().size());
        assertTrue(pictureRepository.findPictureByUrl(newPicture.getUrl()).isEmpty());
    }

    @Test
    public void testProductAddOpenAddForm() throws Exception {

        mockMvc
                .perform(get("/products/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-add"))
                .andExpect(model().attributeExists("productModel"))
                .andExpect(model().attribute("categories", CategoryNameEnum.values()));
    }

    @Test
    public void testProductAddWithValidationErrors() throws Exception {
        ProductAddBindingModel productModel = new ProductAddBindingModel()
                .setName("")
                .setDescription("description of test product")
                .setCategory(CategoryNameEnum.Electronics.name())
                .setQuantity(15)
                .setPrice(BigDecimal.valueOf(150.55));

        mockMvc
                .perform(post("/products/add")
                        .param("name", productModel.getName())
                        .param("description", productModel.getDescription())
                        .param("category", productModel.getCategory())
                        .param("quantity", productModel.getQuantity().toString())
                        .param("price", productModel.getPrice().toString())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("add"))
                .andExpect(flash().attributeExists("productModel"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.productModel"));
    }

    @Test
    public void testProductAddShouldAddProduct() throws Exception {
        ProductAddBindingModel productModel = new ProductAddBindingModel()
                .setName("test product")
                .setDescription("description of test product")
                .setCategory(CategoryNameEnum.Electronics.name())
                .setQuantity(15)
                .setPrice(BigDecimal.valueOf(150.55));

        mockMvc
                .perform(post("/products/add")
                        .param("name", productModel.getName())
                        .param("description", productModel.getDescription())
                        .param("category", productModel.getCategory())
                        .param("quantity", productModel.getQuantity().toString())
                        .param("price", productModel.getPrice().toString())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("own"));

        User currentUser = userRepository.findByUsername("admin").get();
        Product addedProduct = currentUser.getProducts().stream()
                .filter(pr -> pr.getName().equals(productModel.getName()))
                .toList().get(0);

        assertTrue(productRepository.existsById(addedProduct.getId()));
        assertEquals(productModel.getName(), addedProduct.getName());
        assertEquals(productModel.getDescription(), addedProduct.getDescription());
        assertEquals(productModel.getCategory(), addedProduct.getCategory().getName().name());
        assertEquals(productModel.getQuantity(), addedProduct.getQuantity());
        assertEquals(productModel.getPrice(), addedProduct.getPrice());
    }


    @Test
    public void testProductDelete() throws Exception {

        Product newProduct = testDataHelper.initNewProductForUser1();

        mockMvc
                .perform(delete("/products/delete/" + newProduct.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());

    }

    private Product getTestProduct() {
        String productName = "product1";

        return productRepository.findAll().stream()
                .filter(pr -> pr.getName().equals(productName)).findFirst().get();
    }


}