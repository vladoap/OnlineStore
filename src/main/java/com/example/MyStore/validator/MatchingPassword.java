package com.example.MyStore.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MatchingPasswordValidator.class)
public @interface MatchingPassword {

    String message() default "Passwords don't match";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
