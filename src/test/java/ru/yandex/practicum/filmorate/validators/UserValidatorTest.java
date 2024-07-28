package ru.yandex.practicum.filmorate.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;


class UserValidatorTest {

    @Test
    void isUserInfoValid_shouldReturnTrueWhenFieldsAreCorrect() {
        User user = new User(0, "test@yandex.ru", "login", "name",
                LocalDate.of(1995, 4, 28));

        boolean isUserValid = UserValidator.isUserInfoValid(user);

        Assertions.assertTrue(isUserValid);
    }

    @Test
    void isUserInfoValid_shouldThrowValidationExceptionWhen_emailIsNull() {
        User user = new User(0, null, "login", "name",
                LocalDate.of(1995, 4, 28));

        Assertions.assertThrows(ValidationException.class, () -> UserValidator.isUserInfoValid(user));
    }

    @Test
    void isUserInfoValid_shouldThrowValidationExceptionWhen_emailIsBlank() {
        User user = new User(0, "", "login", "name",
                LocalDate.of(1995, 4, 28));

        Assertions.assertThrows(ValidationException.class, () -> UserValidator.isUserInfoValid(user));
    }

    @Test
    void isUserInfoValid_shouldThrowValidationExceptionWhen_emailDoesNotContainAtSymbol() {
        User user = new User(0, "noAtSignHere", "login", "name",
                LocalDate.of(1995, 4, 28));

        Assertions.assertThrows(ValidationException.class, () -> UserValidator.isUserInfoValid(user));
    }

    @Test
    void isUserInfoValid_shouldThrowValidationExceptionWhen_loginIsNull() {
        User user = new User(0, "test@yandex.ru", null, "name",
                LocalDate.of(1995, 4, 28));

        Assertions.assertThrows(ValidationException.class, () -> UserValidator.isUserInfoValid(user));
    }

    @Test
    void isUserInfoValid_shouldThrowValidationExceptionWhen_loginIsBlank() {
        User user = new User(0, "test@yandex.ru", "", "name",
                LocalDate.of(1995, 4, 28));

        Assertions.assertThrows(ValidationException.class, () -> UserValidator.isUserInfoValid(user));
    }

    @Test
    void isUserInfoValid_shouldThrowValidationExceptionWhen_loginContainsSpaces() {
        User user = new User(0, "test@yandex.ru", "Space here", "name",
                LocalDate.of(1995, 4, 28));

        Assertions.assertThrows(ValidationException.class, () -> UserValidator.isUserInfoValid(user));
    }

    @Test
    void isUserInfoValid_shouldThrowValidationExceptionWhen_birthdayDateIsAfterToday() {
        User user = new User(0, "test@yandex.ru", "login", "name",
                LocalDate.of(2030, 4, 28));

        Assertions.assertThrows(ValidationException.class, () -> UserValidator.isUserInfoValid(user));
    }
}
