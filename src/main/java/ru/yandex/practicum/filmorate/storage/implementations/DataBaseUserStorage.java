package ru.yandex.practicum.filmorate.storage.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.storage.mappers.EventRowMapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class DataBaseUserStorage extends BaseStorage<User> implements UserStorage {
    private static final String INSERT_QUERY = "INSERT INTO app_user(email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String FIND_ALL_QUERY = "SELECT u.*, f.friend_id FROM app_user u LEFT JOIN friends f"
            + " ON u.id = f.user_id";
    private static final String FIND_BY_ID_QUERY = "SELECT au.*, f.friend_id FROM app_user au LEFT JOIN friends f ON"
            + " au.id = f.user_id WHERE au.id = ?";
    private static final String UPDATE_QUERY = "UPDATE app_user SET email = ?, login = ?, name = ?, birthday = ?"
            + " WHERE id = ?";
    private static final String FIND_ALL_FRIENDS_QUERY = "SELECT au.*, f2.friend_id FROM app_user au"
            + " LEFT JOIN friends f ON au.id = f.friend_id LEFT JOIN friends"
            + " f2 ON au.id = f2.user_id WHERE f.user_id = ?";
    private static final String ADD_FRIEND_QUERY = "INSERT INTO friends(user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM app_user WHERE id = ?";
    private static final String ADD_EVENT_QUERY = "INSERT INTO events(e_timestamp, user_id, " +
            "operation, type, entity_id) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_EVENT_QUERY = "SELECT * from events WHERE user_id = ?";
    private static final Logger log = LoggerFactory.getLogger(DataBaseUserStorage.class);

    public DataBaseUserStorage(JdbcTemplate jdbc, ResultSetExtractor<List<User>> listExtractor) {
        super(listExtractor, jdbc);
    }

    @Override
    public Collection<User> getAllUsers() {
        return findManyExtractor(FIND_ALL_QUERY);
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
        return findOneExtractor(FIND_BY_ID_QUERY, id);
    }

    public Collection<User> getAllFriends(Integer id) {
        return findManyExtractor(FIND_ALL_FRIENDS_QUERY, id);
    }

    public Optional<User> addFriend(Integer userId, Integer friendId) {
        if (friendshipExists(userId, friendId)) {
            throw new DuplicatedDataException("Запись дружбы между пользователями уже существует.");
        }
        insert(ADD_FRIEND_QUERY, userId, friendId);
        return findUserById(userId);
    }

    @Override
    public Optional<User> deleteFriend(Integer userId, Integer friendId) {
        delete(DELETE_FRIEND_QUERY, userId, friendId);
        return findUserById(userId);
    }

    private boolean friendshipExists(Integer userId, Integer friendId) {
        String checkIfAlreadyFriend = "SELECT au.* FROM app_user au INNER JOIN friends f "
                + "ON au.id = f.friend_id WHERE f.user_id = ? AND f.friend_id = ?";
        Optional<User> friendOpt = findOneExtractor(checkIfAlreadyFriend, userId, friendId);

        return friendOpt.isPresent();
    }

    @Override
    public void deleteUserById(int id) {
        delete(DELETE_USER_QUERY, id);
    }

    @Override
    public void addEvent(Integer userId, Integer entityId, EventType type, OperationType operation) {
        int id = insert(ADD_EVENT_QUERY, LocalDateTime.now(), userId, operation.toString(), type.toString(), entityId);
        log.info("Добавлено событие в ленту с id = {}, UID = {}, entity = {}, type = {}, operation = {}.",
                id, userId, entityId, type, operation);
    }

    @Override
    public Collection<Event> getFeed(Integer userId) {
        return jdbc.query(GET_EVENT_QUERY, new EventRowMapper(), userId);
    }
}
