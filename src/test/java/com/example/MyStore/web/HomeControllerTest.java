package com.example.MyStore.web;

import com.example.MyStore.helper.TestDataHelper;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.entity.User;
import com.example.MyStore.model.view.ProductLatestViewModel;
import com.example.MyStore.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser("admin")
@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private TestDataHelper testDataHelper;


    @BeforeEach
    public void setUp() {
        testDataHelper.getUser1();
        testDataHelper.initProducts();
        testDataHelper.initNewProductForUser1();
        testDataHelper.initNewProductForUser2();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        pictureRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        userRoleRepository.deleteAll();
    }

    @WithAnonymousUser
    @Test
    public void testIndexAnonymous() throws Exception {
        mockMvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void testIndexAuthenticated() throws Exception {
        mockMvc
                .perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testAbout() throws Exception {
        mockMvc
                .perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("about"));
    }

    @Test
    public void testContact() throws Exception {
        mockMvc
                .perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHome() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("latestProducts"));

        List<ProductLatestViewModel> latestProducts = (List<ProductLatestViewModel>) resultActions
                .andReturn()
                .getModelAndView()
                .getModel()
                .get("latestProducts");

        List<Product> allProducts = productRepository.findAll();

        assertEquals(3, latestProducts.size());
        assertEquals(4, allProducts.size());

        for (int i = 0; i < latestProducts.size(); i++) {
            ProductLatestViewModel currentProductModel = latestProducts.get(i);
            Product currentProduct = allProducts.get(i);

            assertEquals(currentProduct.getId(), currentProductModel.getId());
            assertEquals(currentProduct.getName(), currentProductModel.getName());
            assertEquals(currentProduct.getDescription(), currentProductModel.getDescription());
            if (currentProduct.getPictures() != null && currentProduct.getPictures().size() > 0) {
                assertEquals(currentProduct.getPictures().get(0).getUrl(), currentProductModel.getImageUrl());
            }
            assertEquals(currentProduct.getPrice(), currentProductModel.getPrice());
        }
    }

}