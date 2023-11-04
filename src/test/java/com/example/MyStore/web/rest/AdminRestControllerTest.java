package com.example.MyStore.web.rest;

import com.example.MyStore.helper.TestDataHelper;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.entity.User;
import com.example.MyStore.model.enums.UserRoleEnum;
import com.example.MyStore.repository.*;
import com.example.MyStore.service.UserRoleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WithMockUser(value = "admin", roles = "ADMIN")
@SpringBootTest
@AutoConfigureMockMvc
class AdminRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private TestDataHelper testDataHelper;

    @BeforeEach
    public void setUp() {
        testDataHelper.getUser1();
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
    public void testGetAllOffers() throws Exception {
        mockMvc
                .perform(get("/api/admin/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(2)))
                .andExpect(jsonPath("$.pages", hasSize(1)));
    }

    @Test
    public void testDeleteProductWhenProductHasValidId() throws Exception {
        Product product = testDataHelper.initNewProductForUser1();

        assertTrue(productRepository.findById(product.getId()).isPresent());

        mockMvc
                .perform(delete("/api/admin/products/delete/" + product.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        assertTrue(productRepository.findById(product.getId()).isEmpty());
    }

    @Test
    public void testDeleteProductWhenProductNotFound() throws Exception {
        long invalidProductId = 9999;

        mockMvc
                .perform(delete("/api/admin/products/delete/" + invalidProductId)
                        .with(csrf()))
                .andExpect(status().isNotFound());

        assertTrue(productRepository.findById(invalidProductId).isEmpty());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = testDataHelper.getUser1();
        User user2 = testDataHelper.getUser2();

        mockMvc
                .perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(user1.getId().intValue())))
                .andExpect(jsonPath("$.[0].profilePicture", is(user1.getProfilePicture().getUrl())))
                .andExpect(jsonPath("$.[0].fullName", is(user1.getFirstName() + " " + user1.getLastName())))
                .andExpect(jsonPath("$.[0].username", is(user1.getUsername())))
                .andExpect(jsonPath("$.[0].role",
                        is(user1.getRoles().stream().anyMatch(r -> r.getName().equals(UserRoleEnum.ADMIN))
                                ? UserRoleEnum.ADMIN.name()
                                : UserRoleEnum.USER.name())))
                .andExpect(jsonPath("$.[0].email", is(user1.getEmail())))
                .andExpect(jsonPath("$.[0].productsCount", is(user1.getProducts().size())))
                .andExpect(jsonPath("$.[0].ordersCount", is(user1.getOrders().size())))

                .andExpect(jsonPath("$.[1].id", is(user2.getId().intValue())))
                .andExpect(jsonPath("$.[1].profilePicture", is(user2.getProfilePicture().getUrl())))
                .andExpect(jsonPath("$.[1].fullName", is(user2.getFirstName() + " " + user2.getLastName())))
                .andExpect(jsonPath("$.[1].username", is(user2.getUsername())))
                .andExpect(jsonPath("$.[1].role",
                        is(user2.getRoles().stream().anyMatch(r -> r.getName().equals(UserRoleEnum.ADMIN))
                                ? UserRoleEnum.ADMIN.name()
                                : UserRoleEnum.USER.name())))
                .andExpect(jsonPath("$.[1].email", is(user2.getEmail())))
                .andExpect(jsonPath("$.[1].productsCount", is(user2.getProducts().size())))
                .andExpect(jsonPath("$.[1].ordersCount", is(user2.getOrders().size())));
    }

    @Test
    public void testPromoteToAdminWhenIsUser() throws Exception {
        User user2 = testDataHelper.getUser2();
        user2.getRoles().removeIf(r -> r.getName().equals(UserRoleEnum.ADMIN));
        userRepository.save(user2);

        assertFalse(user2.getRoles().stream().anyMatch(r -> r.getName().equals(UserRoleEnum.ADMIN)));

        mockMvc
                .perform(patch("/api/admin/users/promote/" + user2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role", is(UserRoleEnum.ADMIN.name())
                ));
    }

    @Test
    public void testPromoteToAdminWhenIsAlreadyAdmin() throws Exception {
        User user2 = testDataHelper.getUser2();

        assertTrue(user2.getRoles().stream().anyMatch(r -> r.getName().equals(UserRoleEnum.ADMIN)));

        mockMvc
                .perform(patch("/api/admin/users/promote/" + user2.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUser() throws Exception {
        User user2 = testDataHelper.getUser2();

        mockMvc
                .perform(delete("/api/admin/users/delete/" + user2.getId()))
                .andExpect(status().isNoContent());

        assertTrue(userRepository.findById(user2.getId()).isEmpty());
    }

    @Test
    public void testIsCurrentlyLoggedUserWhenIs() throws Exception {
        User user1 = testDataHelper.getUser1();

        mockMvc
                .perform(get("/api/admin/user/" + user1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }


    @Test
    public void testIsCurrentlyLoggedUserWhenIsNot() throws Exception {
        User user2 = testDataHelper.getUser2();

        mockMvc
                .perform(get("/api/admin/user/" + user2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }


}