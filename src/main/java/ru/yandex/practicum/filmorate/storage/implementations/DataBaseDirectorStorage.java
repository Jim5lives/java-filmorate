package ru.yandex.practicum.filmorate.storage.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.interfaces.DirectorStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class DataBaseDirectorStorage extends BaseStorage<Director> implements DirectorStorage {
    private static final String INSERT_DIRECTOR_QUERY = "INSERT INTO director (name) VALUES (?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM director ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM director WHERE id = ?";
    private static final String UPDATE_DIRECTOR_QUERY = "UPDATE director SET name = ? WHERE id = ?";
    private static final String DELETE_DIRECTOR_QUERY = "DELETE FROM director WHERE id = ?";

    public DataBaseDirectorStorage(JdbcTemplate jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Director> getAllDirectors() {
        return findManyMapper(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Director> findDirectorById(Integer id) {
        return findOneMapper(FIND_BY_ID_QUERY, id);

    }

    @Override
    public Director addDirector(Director director) {
        Integer id = insert(
                INSERT_DIRECTOR_QUERY,
                director.getName());
        director.setId(id);
        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        update(
                UPDATE_DIRECTOR_QUERY,
                director.getName(),
                director.getId()
        );
        return director;
    }

    @Override
    public void deleteDirector(int id) {
        delete(DELETE_DIRECTOR_QUERY, id);
    }

    @Override
    public int countExistingDirectorsFromList(List<Integer> directorsInQuestion) {
        String inSql = String.join(",", Collections.nCopies(directorsInQuestion.size(), "?"));
        Integer result = jdbc.queryForObject(String.format("SELECT COUNT(*) AS dircount FROM director WHERE id IN (%s)",
                        inSql),
                Integer.class, directorsInQuestion.toArray());

        if (result == null) {
            log.error("Error counting directors");
            throw new IllegalStateException("Can't count directors");
        } else {
            return result;
        }
    }

}

