package com.example.MyStore.validation;

import com.example.MyStore.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserService userService;

    public UniqueEmailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        // null values are considered as valid. Add additional @NotNull annotation
        if (email == null) {
            return true;
        }

       return userService.isEmailFree(email);
    }
}
