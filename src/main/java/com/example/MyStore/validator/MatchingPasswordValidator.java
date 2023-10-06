package com.example.MyStore.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class MatchingPasswordValidator implements ConstraintValidator<MatchingPassword, Object> {



    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        try {
            Field passwordField = value.getClass().getDeclaredField("password");
            Field confirmPasswordField = value.getClass().getDeclaredField("confirmPassword");

            passwordField.setAccessible(true);
            confirmPasswordField.setAccessible(true);

            String password = (String) passwordField.get(value);
            String confirmPassword = (String) confirmPasswordField.get(value);

            return password != null && password.equals(confirmPassword);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
