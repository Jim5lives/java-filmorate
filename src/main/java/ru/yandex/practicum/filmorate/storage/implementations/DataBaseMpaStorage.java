package ru.yandex.practicum.filmorate.storage.implementations;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class DataBaseMpaStorage extends BaseStorage<Mpa> implements MpaStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = ?";


    public DataBaseMpaStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        return findManyMapper(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Mpa> findMpaById(Integer id) {
        return findOneMapper(FIND_BY_ID_QUERY, id);
    }
}
