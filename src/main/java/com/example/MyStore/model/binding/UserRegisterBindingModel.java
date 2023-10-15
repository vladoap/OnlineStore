package com.example.MyStore.model.binding;

import com.example.MyStore.model.enums.TitleEnum;
import com.example.MyStore.validation.MatchingPassword;
import com.example.MyStore.validation.UniqueUsername;
import jakarta.validation.constraints.*;


@MatchingPassword(passwordField = "password", confirmPasswordField = "confirmPassword")
public class UserRegisterBindingModel {

    private String username;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String email;
    private TitleEnum title;
    private String country;
    private String city;
    private String streetName;
    private Integer streetNumber;
    private Boolean termsAccepted;

    @UniqueUsername
    @NotBlank
    @Size(min = 3, message = "Username must be at least 3 symbols.")
    public String getUsername() {
        return username;
    }

    public UserRegisterBindingModel setUsername(String username) {
        this.username = username;
        return this;
    }

    @NotBlank
    @Size(min = 3, message = "Password must be at least 3 symbols.")
    public String getPassword() {
        return password;
    }

    public UserRegisterBindingModel setPassword(String password) {
        this.password = password;
        return this;
    }

    @NotBlank
    @Size(min = 3, message = "Password must be at least 3 symbols.")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public UserRegisterBindingModel setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    @NotBlank
    @Size(min = 2, message = "First name must be at least 2 symbols.")
    public String getFirstName() {
        return firstName;
    }

    public UserRegisterBindingModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    @NotBlank
    @Size(min = 2, message = "Last name must be at least 2 symbols.")
    public String getLastName() {
        return lastName;
    }

    public UserRegisterBindingModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @NotBlank
    @Email(message = "Enter valid email.")
    public String getEmail() {
        return email;
    }

    public UserRegisterBindingModel setEmail(String email) {
        this.email = email;
        return this;
    }

    @NotNull(message = "Select title.")
    public TitleEnum getTitle() {
        return title;
    }

    public UserRegisterBindingModel setTitle(TitleEnum title) {
        this.title = title;
        return this;
    }

    @NotBlank
    @Size(min = 2, message = "Country must be at least 2 symbols.")
    public String getCountry() {
        return country;
    }

    public UserRegisterBindingModel setCountry(String country) {
        this.country = country;
        return this;
    }

    @NotBlank
    @Size(min = 2, message = "City must be at least 2 symbols.")
    public String getCity() {
        return city;
    }

    public UserRegisterBindingModel setCity(String city) {
        this.city = city;
        return this;
    }

    @NotBlank
    @Size(min = 3, message = "Street name must be at least 3 symbols.")
    public String getStreetName() {
        return streetName;
    }

    public UserRegisterBindingModel setStreetName(String streetName) {
        this.streetName = streetName;
        return this;
    }

    @NotNull
    @Positive(message = "Enter valid street number.")
    public Integer getStreetNumber() {
        return streetNumber;
    }

    public UserRegisterBindingModel setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }

    @AssertTrue(message = "You must accept the Terms and Conditions to proceed.")
    public Boolean getTermsAccepted() {
        return termsAccepted;
    }

    public UserRegisterBindingModel setTermsAccepted(Boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
        return this;
    }


}
