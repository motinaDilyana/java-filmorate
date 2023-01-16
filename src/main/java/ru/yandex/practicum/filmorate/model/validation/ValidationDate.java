package ru.yandex.practicum.filmorate.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidationDate {
    String message() default "The date cannot be less than the set one";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
