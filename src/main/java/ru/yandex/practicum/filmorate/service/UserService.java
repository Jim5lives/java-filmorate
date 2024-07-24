package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.EventDto;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;

import java.util.Collection;

public interface UserService {

    UserDto findUserById(Integer id);

    Collection<UserDto> getAllUsers();

    UserDto createUser(NewUserRequest user);

    UserDto updateUser(UpdateUserRequest updatedUser);

    Collection<UserDto> getAllFriends(Integer id);

    UserDto addFriend(Integer userId, Integer friendId);

    UserDto deleteFriend(Integer userId, Integer friendId);

    Collection<UserDto> getMutualFriends(Integer userId, Integer otherId);

    void deleteUserById(int id);

    Collection<EventDto> getFeed(Integer userId);

    Collection<FilmDto> getRecommendations(Integer id);
}
