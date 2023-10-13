package com.example.MyStore.model.binding;

import com.example.MyStore.validator.MatchingPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@MatchingPassword(passwordField = "newPassword", confirmPasswordField = "confirmNewPassword")
public class UserPasswordChangeBindingModel {


    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;

    @NotBlank
    @Size(min = 3, message = "Password must be at least 3 symbols.")
    public String getOldPassword() {
        return oldPassword;
    }

    public UserPasswordChangeBindingModel setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }

    @NotBlank
    @Size(min = 3, message = "New password must be at least 3 symbols.")
    public String getNewPassword() {
        return newPassword;
    }

    public UserPasswordChangeBindingModel setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

    @NotBlank
    @Size(min = 3, message = "New password must be at least 3 symbols.")
    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public UserPasswordChangeBindingModel setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
        return this;
    }
}
