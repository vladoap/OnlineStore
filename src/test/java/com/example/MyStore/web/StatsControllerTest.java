package com.example.MyStore.web;

import com.example.MyStore.helper.TestDataHelper;
import com.example.MyStore.model.entity.User;
import com.example.MyStore.repository.UserRepository;
import com.example.MyStore.repository.UserRoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(value = "admin", roles = "ADMIN")
@SpringBootTest
@AutoConfigureMockMvc
class StatsControllerTest {

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
    public void testGetStatisticsWithLoggedUser() throws Exception {
        mockMvc
                .perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(view().name("statistics"))
                .andExpect(model().attribute("stats", hasProperty("anonymousRequests", greaterThan(0) )))
                .andExpect(model().attribute("stats", hasProperty("authRequests", greaterThan(0) )));

    }



}