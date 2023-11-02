package com.example.MyStore.web;

import com.example.MyStore.model.binding.UserRegisterBindingModel;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.entity.User;
import com.example.MyStore.model.entity.UserRole;
import com.example.MyStore.model.enums.TitleEnum;
import com.example.MyStore.model.enums.UserRoleEnum;
import com.example.MyStore.repository.PictureRepository;
import com.example.MyStore.repository.UserRepository;
import com.example.MyStore.repository.UserRoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        pictureRepository.deleteAll();
        userRoleRepository.deleteAll();
    }

    @Test
    public void testOpenRegisterForm() throws Exception {
        mockMvc
                .perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth-register"));
    }

    @Test
    public void testRegisterUser() throws Exception {
        initDefaultPicture();
        initRoles();

        UserRegisterBindingModel userModel = createUserRegisterModel();

        mockMvc.perform(post("/users/register")
                        .param("username", userModel.getUsername())
                        .param("title", userModel.getTitle().name())
                        .param("firstName", userModel.getFirstName())
                        .param("lastName", userModel.getLastName())
                        .param("password", userModel.getPassword())
                        .param("confirmPassword", userModel.getConfirmPassword())
                        .param("email", userModel.getEmail())
                        .param("country", userModel.getCountry())
                        .param("city", userModel.getCity())
                        .param("streetName", userModel.getStreetName())
                        .param("streetNumber", String.valueOf(userModel.getStreetNumber()))
                        .param("termsAccepted", String.valueOf(userModel.getTermsAccepted()))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection());

        assertEquals(1, userRepository.count());

        Optional<User> newlyCreatedUserOpt = userRepository.findByUsername(userModel.getUsername());

        assertTrue(newlyCreatedUserOpt.isPresent());

        User newlyCreatedUser = newlyCreatedUserOpt.get();

        assertEquals(userModel.getUsername(), newlyCreatedUser.getUsername());
        assertEquals(userModel.getTitle(), newlyCreatedUser.getTitle());
        assertEquals(userModel.getFirstName(), newlyCreatedUser.getFirstName());
        assertEquals(userModel.getLastName(), newlyCreatedUser.getLastName());
        assertEquals(userModel.getEmail(), newlyCreatedUser.getEmail());
        assertEquals(userModel.getCountry(), newlyCreatedUser.getAddress().getCountry());
        assertEquals(userModel.getCity(), newlyCreatedUser.getAddress().getId().getCity());
        assertEquals(userModel.getStreetName(), newlyCreatedUser.getAddress().getId().getStreetName());
        assertEquals(userModel.getStreetNumber(), newlyCreatedUser.getAddress().getId().getStreetNumber());
        assertTrue(passwordEncoder.matches(userModel.getPassword(), newlyCreatedUser.getPassword()));
    }

    @Test
    public void testRegisterUserWithValidationErrors() throws Exception {
        UserRegisterBindingModel userModel = createUserRegisterModel();

        userModel.setConfirmPassword("invalid_Password");

        mockMvc.perform(post("/users/register")
                        .param("username", userModel.getUsername())
                        .param("title", userModel.getTitle().name())
                        .param("firstName", userModel.getFirstName())
                        .param("lastName", userModel.getLastName())
                        .param("password", userModel.getPassword())
                        .param("confirmPassword", userModel.getConfirmPassword())
                        .param("email", userModel.getEmail())
                        .param("country", userModel.getCountry())
                        .param("city", userModel.getCity())
                        .param("streetName", userModel.getStreetName())
                        .param("streetNumber", String.valueOf(userModel.getStreetNumber()))
                        .param("termsAccepted", String.valueOf(userModel.getTermsAccepted()))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("userRegisterBindingModel"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.userRegisterBindingModel"));
    }

    private UserRegisterBindingModel createUserRegisterModel() {
        String username = "user123";
        String firstName = "First name";
        String lastName = "Last name";
        String password = "123456";
        String confirmPassword = "123456";
        String email = "user10@user.com";
        TitleEnum title = TitleEnum.Mr;
        String country = "Bulgaria";
        String city = "Plovdiv";
        String streetName = "Dimitar Talev";
        Integer streetNumber = 30;
        Boolean areTermsAccepted = true;

        return new UserRegisterBindingModel()
                .setUsername(username)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setPassword(password)
                .setConfirmPassword(confirmPassword)
                .setEmail(email)
                .setTitle(title)
                .setCountry(country)
                .setCity(city)
                .setStreetName(streetName)
                .setStreetNumber(streetNumber)
                .setTermsAccepted(areTermsAccepted);
    }

    private void initDefaultPicture() {
        Picture defaultPicture = new Picture()
                .setTitle("Default Profile")
                .setUrl("defaultPicture/url")
                .setPublicId("publicIdOfThePicture");
        defaultPicture.setCreated(LocalDateTime.now());

        pictureRepository.save(defaultPicture);
    }

    private void initRoles() {
        UserRole adminRole = new UserRole()
                .setName(UserRoleEnum.ADMIN);

        UserRole userRole = new UserRole()
                .setName(UserRoleEnum.USER);

        userRoleRepository.saveAll(List.of(adminRole, userRole));
    }


}