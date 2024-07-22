package ru.yandex.practicum.filmorate.dbtests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.*;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.storage.mappers.*;

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
class GenreDBTests {
    private final DataBaseGenreStorage genreStorage;

    @Test
    void testGetAllGenres() {
        Collection<Genre> allGenres = genreStorage.getAllGenres();

        Assertions.assertEquals(6, allGenres.size());
    }

    @Test
    void testFindGenreById() {
        Optional<Genre> genreOpt = genreStorage.findGenreById(1);

        assertThat(genreOpt)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 1)
                );
    }
}

