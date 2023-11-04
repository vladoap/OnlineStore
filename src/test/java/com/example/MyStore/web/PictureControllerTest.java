package com.example.MyStore.web;

import com.example.MyStore.helper.TestDataHelper;
import com.example.MyStore.model.binding.PictureAddBindingModel;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.entity.User;
import com.example.MyStore.repository.*;
import com.example.MyStore.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.apache.http.client.methods.RequestBuilder.post;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser("admin")
@SpringBootTest
@AutoConfigureMockMvc
class PictureControllerTest {

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

    @Test
    public void testAddProductsPictureWithValidationErrors() throws Exception {
        Product testProduct = testDataHelper.initNewProductForUser1();

        MockMultipartFile mockFile = new MockMultipartFile(
                "picture",
                "newPic.png",
                "image/png",
                new byte[0]);

        System.out.println(mockFile);

        mockMvc
                .perform(multipart(HttpMethod.POST, "/pictures/add/product/" + testProduct.getId())
                        .file(mockFile)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("pictureModel"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.pictureModel"));
    }

    @Test
    public void testAddProductsPicture() throws Exception {
        Product testProduct = testDataHelper.initNewProductForUser1();

        File file = ResourceUtils.getFile("src/main/resources/static/images/feature_prod_01.jpg");
        byte[] imageBytes = Files.readAllBytes(file.toPath());

        MockMultipartFile mockFile = new MockMultipartFile(
                "picture",
                "newPic.png",
                "image/png",
                imageBytes);

        mockMvc
                .perform(multipart(HttpMethod.POST, "/pictures/add/product/" + testProduct.getId())
                        .file(mockFile)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection());

        Optional<Picture> newlyAddedPicture = pictureRepository.findPictureByTitle(mockFile.getOriginalFilename());

        assertTrue(newlyAddedPicture.isPresent());

        boolean isPictureAddedToProduct = productRepository.findById(testProduct.getId()).get().getPictures()
                .contains(newlyAddedPicture.get());

        assertTrue(isPictureAddedToProduct);
    }

}