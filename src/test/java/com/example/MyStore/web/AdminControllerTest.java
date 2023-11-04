package com.example.MyStore.web;

import com.example.MyStore.helper.TestDataHelper;
import com.example.MyStore.repository.UserRepository;
import com.example.MyStore.repository.UserRoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WithMockUser(value = "admin", roles = "ADMIN")
@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private TestDataHelper testDataHelper;

    @BeforeEach
    public void setUp() {
        testDataHelper.getUser1();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        userRoleRepository.deleteAll();
    }

    @Test
    public void testGetAllProductsAsAdmin() throws Exception {
        mockMvc
                .perform(get("/admin/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-offers"));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        mockMvc
                .perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-users-list"));
    }

}