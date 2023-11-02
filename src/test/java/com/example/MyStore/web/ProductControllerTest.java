package com.example.MyStore.web;

import com.example.MyStore.helper.TestDataHelper;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.entity.User;
import com.example.MyStore.model.view.ProductsSummaryViewModel;
import com.example.MyStore.repository.CategoryRepository;
import com.example.MyStore.repository.PictureRepository;
import com.example.MyStore.repository.ProductRepository;
import com.example.MyStore.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Array;
import java.util.List;

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
    private TestDataHelper testDataHelper;

    private User testUser;

    private List<Product> testProducts;

    @BeforeEach
    public void setUp() {
        testUser = testDataHelper.initUser1();
        testProducts = testDataHelper.initProducts();
    }

    @AfterEach
    public void tearDown() {
//        pictureRepository.deleteAll();
//        productRepository.deleteAll();
//        categoryRepository.deleteAll();
        userRepository.deleteAll();

    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllProductsExceptOwn() throws Exception {

        Product notOwnProduct = testProducts
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
        assertEquals(notOwnProduct.getPictures().stream().findFirst().get().getUrl(), productModel.getImageUrl());
        assertEquals(notOwnProduct.getQuantity(), productModel.getQuantity());
        assertEquals(notOwnProduct.getPrice(), productModel.getPrice());
        assertNotEquals(notOwnProduct.getSeller(), testUser);

    }


}