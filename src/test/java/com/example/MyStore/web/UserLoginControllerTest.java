package com.example.MyStore.web;

import com.example.MyStore.repository.UserRepository;
import com.example.MyStore.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    @AfterEach
    public void tearDown () {
        userRepository.deleteAll();
    }

    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth-login"));
    }

    @Test
    public void testLoginError () throws Exception {
        String usernameToTest = "vlado";

        mockMvc.perform(post("/users/login-error")
                .param("username", usernameToTest)
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("username", usernameToTest))
                .andExpect(flash().attribute("bad_credentials", true));
    }

}