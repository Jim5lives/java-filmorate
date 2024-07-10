package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Repository
@Primary
public class DataBaseUserStorage extends BaseStorage<User> implements UserStorage {
    private static final String INSERT_QUERY = "INSERT INTO app_user(email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM app_user";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM app_user WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE app_user SET email = ?, login = ?, name = ?, birthday = ?" +
            " WHERE id = ?";
    private static final String FIND_ALL_FRIENDS_QUERY = "SELECT au.* FROM app_user au JOIN friends f "
            + "ON au.id = f.friend_id WHERE f.user_id = ?";
    private static final String ADD_FRIEND_QUERY = "INSERT INTO friends(user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

    public DataBaseUserStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<User> getAllUsers() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public User createUser(User user) {
        Integer id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public Optional<User> findUserById(Integer id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public Collection<User> getAllFriends(Integer id) {
        return findMany(
                FIND_ALL_FRIENDS_QUERY,
                id
        );
    }

    public Optional<User> addFriend(Integer friendId, Integer userId) {
        if (friendshipExists(userId, friendId)) {
            throw new DuplicatedDataException("Запись дружбы между пользователями уже существует.");
        }
        insert(
                ADD_FRIEND_QUERY,
                friendId,
                userId

        );
        return findUserById(userId);
    }

    @Override
    public Optional<User> deleteFriend(Integer userId, Integer friendId) {
         delete(
                 DELETE_FRIEND_QUERY,
                userId,
                friendId
         );
        return findUserById(userId);
    }

    private boolean friendshipExists(Integer userId, Integer friendId) {
        String checkIfAlreadyFriend = "SELECT au.* FROM app_user au INNER JOIN friends f "
                + "ON au.id = f.friend_id WHERE f.user_id = ? AND f.friend_id = ?";
            Optional<User> friendOpt = findOne(checkIfAlreadyFriend, userId, friendId);

        return friendOpt.isPresent();
    }
}
