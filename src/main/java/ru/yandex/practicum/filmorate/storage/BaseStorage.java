package ru.yandex.practicum.filmorate.storage;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exceptions.CreateException;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class BaseStorage<T> {
    protected JdbcTemplate jdbc;
    protected RowMapper<T> mapper;
    protected ResultSetExtractor<List<T>> listExtractor;

    public BaseStorage(JdbcTemplate jdbc, RowMapper<T> mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    public BaseStorage(ResultSetExtractor<List<T>> listExtractor, JdbcTemplate jdbc) {
        this.listExtractor = listExtractor;
        this.jdbc = jdbc;
    }

    public BaseStorage(JdbcTemplate jdbc, RowMapper<T> mapper, ResultSetExtractor<List<T>> listExtractor) {
        this.jdbc = jdbc;
        this.mapper = mapper;
        this.listExtractor = listExtractor;
    }

    protected Integer insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);

        Integer id = keyHolder.getKeyAs(Integer.class);

        if (id != null) {
            return id;
        } else {
            throw new CreateException("Не удалось сохранить данные");
        }
    }

    protected List<T> findManyExtractor(String query, Object... params) {
        return jdbc.query(query, listExtractor, params);
    }

    protected List<T> findManyMapper(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    protected Optional<T> findOneExtractor(String query, Object... params) {
        try {
            List<T> results = jdbc.query(query, listExtractor, params);
            assert results != null;
            if (results.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.ofNullable(results.getFirst());
            }
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    protected Optional<T> findOneMapper(String query, Object... params) {
        try {
            T result = jdbc.queryForObject(query, mapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }


    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated == 0) {
            throw new UpdateException("Не удалось обновить данные");
        }
    }

    protected void delete(String query, Object... params) {
        jdbc.update(query, params);
    }

}