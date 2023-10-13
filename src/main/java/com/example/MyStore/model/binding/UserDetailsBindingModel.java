package com.example.MyStore.model.binding;

import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.enums.TitleEnum;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public class UserDetailsBindingModel {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private TitleEnum title;
    private String profilePicture;
    private String country;
    private String city;
    private String streetName;
    private Integer streetNumber;
    private MultipartFile newProfilePicture;


    @NotBlank
    @Size(min = 3, message = "Username must be at least 3 symbols.")
    public String getUsername() {
        return username;
    }

    public UserDetailsBindingModel setUsername(String username) {
        this.username = username;
        return this;
    }

    @NotBlank
    @Size(min = 3, message = "Password must be at least 3 symbols.")
    public String getPassword() {
        return password;
    }

    public UserDetailsBindingModel setPassword(String password) {
        this.password = password;
        return this;
    }


    @NotBlank
    @Size(min = 2, message = "First name must be at least 2 symbols.")
    public String getFirstName() {
        return firstName;
    }

    public UserDetailsBindingModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }


    @NotBlank
    @Size(min = 2, message = "Last name must be at least 2 symbols.")
    public String getLastName() {
        return lastName;
    }

    public UserDetailsBindingModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }


    @NotBlank
    @Email(message = "Enter valid email.")
    public String getEmail() {
        return email;
    }

    public UserDetailsBindingModel setEmail(String email) {
        this.email = email;
        return this;
    }

    @NotNull(message = "Select title.")
    public TitleEnum getTitle() {
        return title;
    }

    public UserDetailsBindingModel setTitle(TitleEnum title) {
        this.title = title;
        return this;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public UserDetailsBindingModel setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    @NotBlank
    @Size(min = 2, message = "Country must be at least 2 symbols.")
    public String getCountry() {
        return country;
    }

    public UserDetailsBindingModel setCountry(String country) {
        this.country = country;
        return this;
    }

    @NotBlank
    @Size(min = 2, message = "City must be at least 2 symbols.")
    public String getCity() {
        return city;
    }

    public UserDetailsBindingModel setCity(String city) {
        this.city = city;
        return this;
    }

    @NotBlank
    @Size(min = 3, message = "Street name must be at least 3 symbols.")
    public String getStreetName() {
        return streetName;
    }

    public UserDetailsBindingModel setStreetName(String streetName) {
        this.streetName = streetName;
        return this;
    }

    @NotNull
    @Positive(message = "Enter valid street number.")
    public Integer getStreetNumber() {
        return streetNumber;
    }

    public UserDetailsBindingModel setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }

    public MultipartFile getNewProfilePicture() {
        return newProfilePicture;
    }

    public UserDetailsBindingModel setNewProfilePicture(MultipartFile newProfilePicture) {
        this.newProfilePicture = newProfilePicture;
        return this;
    }


}
