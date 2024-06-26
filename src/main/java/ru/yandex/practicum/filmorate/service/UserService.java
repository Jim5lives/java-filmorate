package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;

public interface UserService {

    User findUserById(Integer id);

    Collection<User> getAllUsers();

    User createUser(User user);

    User updateUser(User updatedUser);

    Collection<User> getAllFriends(Integer id);

    User addFriend(Integer userId, Integer friendId);

    User deleteFriend(Integer userId, Integer friendId);

    Collection<User> getMutualFriends(Integer userId, Integer otherId);
}
