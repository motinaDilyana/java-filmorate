package ru.yandex.practicum.filmorate.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<ValidationDate, LocalDate> {
    @Override
    public boolean isValid(LocalDate inputDate, ConstraintValidatorContext context) {
        return inputDate.isAfter(LocalDate.of(1895, 12, 28));
    }
}