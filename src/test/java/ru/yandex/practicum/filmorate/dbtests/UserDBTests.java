package ru.yandex.practicum.filmorate.dbtests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.implementations.FilmServiceImpl;
import ru.yandex.practicum.filmorate.service.implementations.MpaServiceImpl;
import ru.yandex.practicum.filmorate.service.interfaces.FilmService;
import ru.yandex.practicum.filmorate.service.interfaces.MpaService;
import ru.yandex.practicum.filmorate.service.interfaces.UserService;
import ru.yandex.practicum.filmorate.storage.implementations.*;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.storage.mappers.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@JdbcTest
@AutoConfigureTestDatabase
@Sql(scripts = "/testdata.sql")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserStorage.class, DataBaseUserStorage.class, UserResultSetExtractor.class,
        UserListResultSetExtractor.class, UserService.class, FilmService.class, DataBaseGenreStorage.class,
        DataBaseDirectorStorage.class, FilmResultSetExtractor.class, FilmListResultSetExtractor.class,
        FilmServiceImpl.class, GenreRowMapper.class, DirectorRowMapper.class, MpaRowMapper.class, FilmStorage.class,
        DataBaseFilmStorage.class, MpaStorage.class, MpaService.class, MpaServiceImpl.class,
        DataBaseMpaStorage.class, MpaRowMapper.class})
class UserDBTests {
    private final DataBaseUserStorage userStorage;

    @Test
    void testFindUserById() {

        User user = new User();
        user.setName("Test0");
        user.setLogin("TestLogin0");
        user.setEmail("test0@mail.ru");
        user.setBirthday(LocalDate.of(1994, 10, 25));
        user = userStorage.createUser(user);

        Optional<User> userOptional = userStorage.findUserById(user.getId());
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user1 ->
                        assertThat(user1).hasFieldOrPropertyWithValue("id", user1.getId())
                );
    }

    @Test
    void testGetAllUsers() {
        Collection<User> users = userStorage.getAllUsers();

        Assertions.assertEquals(2, users.size());
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setName("Test3");
        user.setLogin("TestLogin3");
        user.setEmail("test3@mail.ru");
        user.setBirthday(LocalDate.of(1994, 10, 25));

        user = userStorage.createUser(user);

        Assertions.assertEquals(20, user.getId());
        Assertions.assertEquals(3, userStorage.getAllUsers().size());
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setName("Test3");
        user.setLogin("TestLogin3");
        user.setEmail("test3@mail.ru");
        user.setBirthday(LocalDate.of(1994, 10, 25));
        user = userStorage.createUser(user);
        User updated = new User();
        updated.setEmail("updated@email");
        updated.setLogin("updatedLogin");
        updated.setName("updatedName");
        updated.setBirthday(LocalDate.of(1950, 10, 20));
        updated.setId(user.getId());

        User result = userStorage.updateUser(updated);

        Assertions.assertEquals("updated@email", result.getEmail());
        Assertions.assertEquals("updatedLogin", result.getLogin());
        Assertions.assertEquals("updatedName", result.getName());
        Assertions.assertEquals(LocalDate.of(1950, 10, 20), result.getBirthday());
    }

    @Test
    void testAddFriend() {
        User user = new User();
        user.setName("Test4");
        user.setLogin("TestLogin4");
        user.setEmail("test4@mail.ru");
        user.setBirthday(LocalDate.of(1994, 10, 25));
        user = userStorage.createUser(user);
        User user2 = new User();
        user2.setName("Test5");
        user2.setLogin("TestLogin5");
        user2.setEmail("test5@mail.ru");
        user2.setBirthday(LocalDate.of(1994, 10, 25));
        user2 = userStorage.createUser(user);

        Optional<User> userOpt = userStorage.addFriend(user.getId(), user2.getId());

        Assertions.assertTrue(userOpt.isPresent());
        Assertions.assertEquals(1, userOpt.get().getFriends().size());
    }

    @Test
    void testGetAllFriends() {
        User user = new User();
        user.setName("Test6");
        user.setLogin("TestLogin6");
        user.setEmail("test6@mail.ru");
        user.setBirthday(LocalDate.of(1994, 10, 25));
        user = userStorage.createUser(user);
        User user2 = new User();
        user2.setName("Test6");
        user2.setLogin("TestLogin6");
        user2.setEmail("test6@mail.ru");
        user2.setBirthday(LocalDate.of(1994, 10, 25));
        user2 = userStorage.createUser(user);
        userStorage.addFriend(user.getId(), user2.getId());

        Collection<User> friends = userStorage.getAllFriends(user.getId());

        Assertions.assertEquals(1, friends.size());
    }

    @Test
    void testDeleteFriend() {
        User user = new User();
        user.setName("Test7");
        user.setLogin("TestLogin7");
        user.setEmail("test7@mail.ru");
        user.setBirthday(LocalDate.of(1994, 10, 25));
        user = userStorage.createUser(user);
        User user2 = new User();
        user2.setName("Test8");
        user2.setLogin("TestLogin8");
        user2.setEmail("test8@mail.ru");
        user2.setBirthday(LocalDate.of(1994, 10, 25));
        user2 = userStorage.createUser(user);
        userStorage.addFriend(user.getId(), user2.getId());

        Optional<User> userOpt = userStorage.deleteFriend(user.getId(), user2.getId());

        Assertions.assertTrue(userOpt.isPresent());
        Assertions.assertEquals(0, userOpt.get().getFriends().size());
    }
}
