package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;

public class DurationValidator implements
        ConstraintValidator<DurationConstraint, Duration> {

    @Override
    public void initialize(DurationConstraint duration) {
    }

    @Override
    public boolean isValid(Duration duration,
                           ConstraintValidatorContext cxt) {
        return duration.isPositive();
    }

}