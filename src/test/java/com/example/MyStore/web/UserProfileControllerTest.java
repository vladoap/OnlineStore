package com.example.MyStore.web;

import com.example.MyStore.helper.TestDataHelper;
import com.example.MyStore.model.binding.UserDetailsBindingModel;
import com.example.MyStore.model.binding.UserPasswordChangeBindingModel;
import com.example.MyStore.model.entity.*;
import com.example.MyStore.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser("admin")
@SpringBootTest
@AutoConfigureMockMvc
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TestDataHelper testDataHelper;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = testDataHelper.getUser1();
        userRepository.save(testUser);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        pictureRepository.deleteAll();
        userRoleRepository.deleteAll();
    }

    @Test
    public void testOpenUserProfileForm() throws Exception {

        mockMvc
                .perform(get("/users/profile/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-profile"))
                .andExpect(model().attributeExists("userDetails"))
                .andExpect(model().attribute("userDetails", hasProperty("username", equalTo(testUser.getUsername()))))
                .andExpect(model().attribute("userDetails", hasProperty("firstName", equalTo(testUser.getFirstName()))))
                .andExpect(model().attribute("userDetails", hasProperty("lastName", equalTo(testUser.getLastName()))))
                .andExpect(model().attribute("userDetails", hasProperty("email", equalTo(testUser.getEmail()))))
                .andExpect(model().attribute("userDetails", hasProperty("title", equalTo(testUser.getTitle()))))
                .andExpect(model().attribute("userDetails", hasProperty("profilePicture", equalTo(testUser.getProfilePicture().getUrl()))))
                .andExpect(model().attribute("userDetails", hasProperty("country", equalTo(testUser.getAddress().getCountry()))))
                .andExpect(model().attribute("userDetails", hasProperty("city", equalTo(testUser.getAddress().getId().getCity()))))
                .andExpect(model().attribute("userDetails", hasProperty("streetName", equalTo(testUser.getAddress().getId().getStreetName()))))
                .andExpect(model().attribute("userDetails", hasProperty("streetNumber", equalTo(testUser.getAddress().getId().getStreetNumber()))));
    }

    @Test
    public void testUserUpdatePatchWithValidationErrors() throws Exception {

        mockMvc.perform(patch("/users/profile/update"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("userDetails"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.userDetails"));

    }

    @Test
    public void testUserUpdatePatchWhenPasswordIncorrect() throws Exception {
        UserDetailsBindingModel userModel = createUserDetailsBindingModel();

        mockMvc.perform(patch("/users/profile/update")
                        .param("username", userModel.getUsername())
                        .param("password", "wrong password")
                        .param("email", userModel.getEmail())
                        .param("firstName", userModel.getFirstName())
                        .param("lastName", userModel.getLastName())
                        .param("title", userModel.getTitle().name())
                        .param("profilePicture", userModel.getProfilePicture())
                        .param("country", userModel.getCountry())
                        .param("city", userModel.getCity())
                        .param("streetName", userModel.getStreetName())
                        .param("streetNumber", String.valueOf(userModel.getStreetNumber()))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("userDetails"))
                .andExpect(flash().attribute("incorrectPassword", true));

    }

    @Test
    public void testUserUpdatePatchNewPicture() throws Exception {

            File file = ResourceUtils.getFile("src/main/resources/static/images/brand_04.png");
            byte[] imageBytes = Files.readAllBytes(file.toPath());

            MockMultipartFile mockFile = new MockMultipartFile(
                    "newProfilePicture",
                    "newPic.png",
                    "image/png",
                    imageBytes);

        UserDetailsBindingModel userModel = createUserDetailsBindingModel();

        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PATCH, "/users/profile/update")
                        .file(mockFile)
                        .param("username", userModel.getUsername())
                        .param("password", userModel.getPassword())
                        .param("email", userModel.getEmail())
                        .param("firstName", userModel.getFirstName())
                        .param("lastName", "new-last-name")
                        .param("title", userModel.getTitle().name())
                        .param("profilePicture", userModel.getProfilePicture())
                        .param("country", userModel.getCountry())
                        .param("city", userModel.getCity())
                        .param("streetName", userModel.getStreetName())
                        .param("streetNumber", String.valueOf(userModel.getStreetNumber()))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("successMessageProfile"));


        Optional<User> updatedUserOpt = userRepository.findById(testUser.getId());
        assertTrue(updatedUserOpt.isPresent());

        User updatedUser = updatedUserOpt.get();

        assertEquals(userModel.getUsername(), updatedUser.getUsername());
        assertEquals(mockFile.getOriginalFilename(), updatedUser.getProfilePicture().getTitle());
        assertNotEquals(userModel.getProfilePicture(), updatedUser.getProfilePicture().getUrl());
        assertEquals("new-last-name", updatedUser.getLastName());
    }

    @Test
    public void testUserUpdatePatchOldPicture() throws Exception {

        UserDetailsBindingModel userModel = createUserDetailsBindingModel();

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/profile/update")
                        .param("username", userModel.getUsername())
                        .param("password", userModel.getPassword())
                        .param("email", userModel.getEmail())
                        .param("firstName", userModel.getFirstName())
                        .param("lastName", "new-last-name")
                        .param("title", userModel.getTitle().name())
                        .param("profilePicture", userModel.getProfilePicture())
                        .param("country", userModel.getCountry())
                        .param("city", userModel.getCity())
                        .param("streetName", userModel.getStreetName())
                        .param("streetNumber", String.valueOf(userModel.getStreetNumber()))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("successMessageProfile"));


        Optional<User> updatedUserOpt = userRepository.findById(testUser.getId());
        assertTrue(updatedUserOpt.isPresent());

        User updatedUser = updatedUserOpt.get();

        assertEquals(userModel.getUsername(), updatedUser.getUsername());
        assertEquals(userModel.getProfilePicture(), updatedUser.getProfilePicture().getUrl());
        assertEquals("new-last-name", updatedUser.getLastName());
    }

    @Test
    public void testChangeUserPasswordWithValidationErrors() throws Exception {
        UserPasswordChangeBindingModel passwordModel = new UserPasswordChangeBindingModel()
                .setOldPassword("")
                .setNewPassword("123")
                .setConfirmNewPassword("123");

        mockMvc
                .perform(patch("/users/profile/password")
                        .param("oldPassword", passwordModel.getOldPassword())
                        .param("newPassword", passwordModel.getNewPassword())
                        .param("confirmNewPassword", passwordModel.getConfirmNewPassword())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("userPasswordChangeBindingModel"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.userPasswordChangeBindingModel"));
    }

    @Test
    public void testChangeUserPasswordWhenPasswordIncorrect() throws Exception {
        UserPasswordChangeBindingModel passwordModel = new UserPasswordChangeBindingModel()
                .setOldPassword("invalid_password")
                .setNewPassword("123")
                .setConfirmNewPassword("123");

        mockMvc
                .perform(patch("/users/profile/password")
                        .param("oldPassword", passwordModel.getOldPassword())
                        .param("newPassword", passwordModel.getNewPassword())
                        .param("confirmNewPassword", passwordModel.getConfirmNewPassword())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("userPasswordChangeBindingModel"))
                .andExpect(flash().attribute("incorrectPasswordChange", true));
    }

    @Test
    public void testChangeUserPasswordWhenNewPasswordLikeOld() throws Exception {
        UserPasswordChangeBindingModel passwordModel = new UserPasswordChangeBindingModel()
                .setOldPassword("123456")
                .setNewPassword("123456")
                .setConfirmNewPassword("123456");

        mockMvc
                .perform(patch("/users/profile/password")
                        .param("oldPassword", passwordModel.getOldPassword())
                        .param("newPassword", passwordModel.getNewPassword())
                        .param("confirmNewPassword", passwordModel.getConfirmNewPassword())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("userPasswordChangeBindingModel"))
                .andExpect(flash().attribute("PasswordIsTheSame", true));
    }

    @Test
    public void testChangeUserPasswordShouldChangePassword() throws Exception {
        String newPassword = "123";

        UserPasswordChangeBindingModel passwordModel = new UserPasswordChangeBindingModel()
                .setOldPassword("123456")
                .setNewPassword(newPassword)
                .setConfirmNewPassword(newPassword);

        mockMvc
                .perform(patch("/users/profile/password")
                        .param("oldPassword", passwordModel.getOldPassword())
                        .param("newPassword", passwordModel.getNewPassword())
                        .param("confirmNewPassword", passwordModel.getConfirmNewPassword())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("successMessagePassword"));

        User updatedUser = userRepository.findById(testUser.getId()).get();
        assertTrue(passwordEncoder.matches(newPassword, updatedUser.getPassword()));
    }


    private UserDetailsBindingModel createUserDetailsBindingModel() {
        return new UserDetailsBindingModel()
                .setUsername(testUser.getUsername())
                .setPassword("123456")
                .setEmail(testUser.getEmail())
                .setTitle(testUser.getTitle())
                .setFirstName(testUser.getFirstName())
                .setLastName(testUser.getLastName())
                .setCountry(testUser.getAddress().getCountry())
                .setCity(testUser.getAddress().getId().getCity())
                .setStreetName(testUser.getAddress().getId().getStreetName())
                .setStreetNumber(testUser.getAddress().getId().getStreetNumber())
                .setProfilePicture(testUser.getProfilePicture().getUrl());
    }


}