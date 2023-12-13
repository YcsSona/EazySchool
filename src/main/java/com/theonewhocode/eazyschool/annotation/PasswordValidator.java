package com.theonewhocode.eazyschool.annotation;

import com.theonewhocode.eazyschool.validations.PasswordStrengthValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
// any custom login to be executed whenever someone uses the annotation
@Constraint(validatedBy = PasswordStrengthValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordValidator {

    String message() default "Please choose a strong password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
