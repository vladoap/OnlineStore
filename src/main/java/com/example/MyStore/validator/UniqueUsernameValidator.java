package com.example.MyStore.validator;

import com.example.MyStore.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserService userService;

    public UniqueUsernameValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        // null values are considered as valid. Add additional @NotNull annotation
        if (username == null) {
            return true;
        }

       return userService.isUsernameFree(username);
    }
}
