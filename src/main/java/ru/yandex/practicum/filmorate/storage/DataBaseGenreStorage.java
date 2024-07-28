package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class DataBaseGenreStorage extends BaseStorage<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genre ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE id = ?";

    public DataBaseGenreStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return findManyMapper(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Genre> findGenreById(Integer id) {
        return findOneMapper(FIND_BY_ID_QUERY, id);

    }

    @Override
    public int countExistingGenresFromList(List<Integer> genresInQuestion) {
        String inSql = String.join(",", Collections.nCopies(genresInQuestion.size(), "?"));
        Integer result = jdbc.queryForObject(String.format("SELECT COUNT(*) AS gencount FROM genre WHERE id IN (%s)",
                        inSql),
                Integer.class, genresInQuestion.toArray());

        if (result == null) {
            log.error("Error counting genres");
            throw new IllegalStateException("Can't count genres");
        } else {
            return result;
        }
    }

}
