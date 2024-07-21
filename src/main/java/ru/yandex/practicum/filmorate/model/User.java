package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.yandex.practicum.filmorate.serialization.LocalDateDeserializer;
import ru.yandex.practicum.filmorate.serialization.LocalDateSerializer;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * User.
 */
@Data
public class User {
    private Integer id;
    private String email;
    private String login;
    private String name;
    private Set<Integer> friends;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthday;

    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.friends = new HashSet<>();
        this.birthday = birthday;
    }

    public User() {
    }

    public void addUserIdToFriendsList(Integer id) {
        friends.add(id);
    }

    public void deleteUserIdFromFriendsList(Integer id) {
        friends.remove(id);
    }
}

