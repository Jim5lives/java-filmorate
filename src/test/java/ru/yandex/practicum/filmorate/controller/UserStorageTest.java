package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.implementations.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class UserStorageTest {
    InMemoryUserStorage userStorage;

    @BeforeEach
    void beforeEach() {
        userStorage = new InMemoryUserStorage();
    }

    @Test
    void findAllUsers_shouldReturnAllUsers() {
        User user1 = new User(1, "test1@yandex.ru", "login1", "name1",
                LocalDate.of(1995, 4, 28));
        User user2 = new User(2, "test2@yandex.ru", "login2", "name2",
                LocalDate.of(1987, 1, 6));
        userStorage.createUser(user1);
        userStorage.createUser(user2);

        Collection<User> allUsers = userStorage.getAllUsers();

        Assertions.assertEquals(2, allUsers.size());
    }

    @Test
    void createUser_shouldCreateNewUser() {
        User userToCreate = new User(0, "test@yandex.ru", "login", "name",
                LocalDate.of(1995, 4, 28));

        userStorage.createUser(userToCreate);

        List<User> userList = new ArrayList<>(userStorage.getAllUsers());
        User createdUser = userList.getFirst();
        Assertions.assertEquals(1, userList.size());
        Assertions.assertEquals(userToCreate.getName(), createdUser.getName());
    }

    @Test
    void createUser_shouldSetLoginAsNameWhenNameIsNull() {
        User user = new User(0, "test@yandex.ru", "login", null,
                LocalDate.of(1995, 4, 28));

        userStorage.createUser(user);

        List<User> userList = new ArrayList<>(userStorage.getAllUsers());
        User createdUser = userList.getFirst();
        Assertions.assertEquals(1, userList.size());
        Assertions.assertEquals("login", createdUser.getName());
    }

    @Test
    void createUser_shouldSetIdInOrder() {
        User user1 = new User(0, "test1@yandex.ru", "login1", "name1",
                LocalDate.of(1995, 4, 28));
        User user2 = new User(0, "test2@yandex.ru", "login2", "name2",
                LocalDate.of(1987, 1, 6));
        User user3 = new User(0, "test3@yandex.ru", "login3", "name3",
                LocalDate.of(1930, 3, 3));

        User createdUser1 = userStorage.createUser(user1);
        User createdUser2 = userStorage.createUser(user2);
        User createdUser3 = userStorage.createUser(user3);

        Assertions.assertEquals(3, userStorage.getAllUsers().size());
        Assertions.assertEquals(1, createdUser1.getId());
        Assertions.assertEquals(2, createdUser2.getId());
        Assertions.assertEquals(3, createdUser3.getId());
    }

    @Test
    void updateUser_ShouldUpdateUserWhenAllFieldsAreValid() {
        User user = new User(1, "test@yandex.ru", "login", "name",
                LocalDate.of(1995, 4, 28));
        userStorage.createUser(user);
        User updatedUser = new User(1, "testUpdated@yandex.ru", "loginUpdated", "nameUpdated",
                LocalDate.of(1993, 1, 30));

        userStorage.updateUser(updatedUser);

        List<User> userList = new ArrayList<>(userStorage.getAllUsers());
        User updatedActual = userList.getFirst();
        Assertions.assertEquals(1, userList.size());
        Assertions.assertEquals("testUpdated@yandex.ru", updatedActual.getEmail());
        Assertions.assertEquals("loginUpdated", updatedActual.getLogin());
        Assertions.assertEquals("nameUpdated", updatedActual.getName());
        Assertions.assertEquals(LocalDate.of(1993, 1, 30), updatedActual.getBirthday());
    }

    @Test
    void updateUser_ShouldThrowValidationExceptionWhenIdIsNull() {
        User user = new User(1, "test@yandex.ru", "login", "name",
                LocalDate.of(1995, 4, 28));
        userStorage.createUser(user);
        User updatedUser = new User(null, "testUpdated@yandex.ru", "loginUpdated", "nameUpdated",
                LocalDate.of(1993, 1, 30));

        Assertions.assertThrows(ValidationException.class, () -> userStorage.updateUser(updatedUser));
    }

    @Test
    void updateUser_ShouldThrowNotFoundExceptionWhenIdNonExistent() {
        User user = new User(1, "test@yandex.ru", "login", "name",
                LocalDate.of(1995, 4, 28));
        userStorage.createUser(user);
        User updatedUser = new User(7, "testUpdated@yandex.ru", "loginUpdated", "nameUpdated",
                LocalDate.of(1993, 1, 30));

        Assertions.assertThrows(NotFoundException.class, () -> userStorage.updateUser(updatedUser));
    }

    @Test
    void updateUser_ShouldThrowDuplicatedDataExceptionWhenEmailAlreadyRegistered() {
        User user = new User(1, "test@yandex.ru", "login1", "name1",
                LocalDate.of(1995, 4, 28));
        User userToUpdate = new User(2, "testtest@yandex.ru", "login2", "name2",
                LocalDate.of(2000, 2, 29));
        userStorage.createUser(user);
        userStorage.createUser(userToUpdate);
        User updatedUser = new User(2, "test@yandex.ru", "loginUpdated", "nameUpdated",
                LocalDate.of(1993, 1, 30));

        Assertions.assertThrows(ValidationException.class, () -> userStorage.updateUser(updatedUser));
    }
}
