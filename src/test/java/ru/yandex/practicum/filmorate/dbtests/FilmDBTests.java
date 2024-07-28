package ru.yandex.practicum.filmorate.dbtests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.implementations.FilmServiceImpl;
import ru.yandex.practicum.filmorate.service.implementations.MpaServiceImpl;
import ru.yandex.practicum.filmorate.service.interfaces.FilmService;
import ru.yandex.practicum.filmorate.service.interfaces.MpaService;
import ru.yandex.practicum.filmorate.service.interfaces.UserService;
import ru.yandex.practicum.filmorate.storage.implementations.*;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.storage.mappers.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
class FilmDBTests {
    private final DataBaseFilmStorage filmStorage;

    @Test
    void testFindFilmById() {
        Mpa mpa = new Mpa();
        mpa.setId(1);
        Set<Genre> genres = new HashSet<>();
        Set<Director> directors = new HashSet<>();

        Film film = new Film();
        film.setName("Test0");
        film.setDescription("testDescription0");
        film.setDuration(Duration.ofMinutes(40));
        film.setReleaseDate(LocalDate.of(1994, 10, 10));
        film.setMpa(mpa);
        film.setGenres(genres);
        film.setDirectors(directors);
        film = filmStorage.createFilm(film);

        Optional<Film> filmOptional = filmStorage.findFilmById(film.getId());
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film1 ->
                        assertThat(film1).hasFieldOrPropertyWithValue("id", film1.getId())
                );
    }

    @Test
    void testGetAllFilms() {
        Collection<Film> films = filmStorage.getAllFilms();

        Assertions.assertEquals(1, films.size());
    }

    /*
    @Test
    void testCreateFilm() {
        Mpa mpa = new Mpa();
        mpa.setId(1);
        Genre genre = new Genre();
        genre.setId(1);
        Set<Genre> genres = new HashSet<>();
        genres.add(genre);
        Director director = new Director();
        director.setId(1);
        Set<Director> directors = new HashSet<>();
        directors.add(director);
        Film film = new Film();
        film.setName("Test3");
        film.setDescription("testDescription3");
        film.setDuration(Duration.ofMinutes(40));
        film.setReleaseDate(LocalDate.of(1994, 10, 10));
        film.setMpa(mpa);
        film.setGenres(genres);
        film.setDirectors(directors);

        film = filmStorage.createFilm(film);

        Assertions.assertTrue(film.getId() != null);
    }

    @Test
    void testUpdateFilm() {
        Mpa mpa = new Mpa();
        mpa.setId(1);
        Genre genre = new Genre();
        genre.setId(1);
        Set<Genre> genres = new HashSet<>();
        genres.add(genre);
        Director director = new Director();
        director.setId(null);
        Set<Director> directors = new HashSet<>();
        directors.add(director);
        Film film = new Film();
        film.setName("Test3");
        film.setDescription("testDescription3");
        film.setDuration(Duration.ofMinutes(40));
        film.setReleaseDate(LocalDate.of(1994, 10, 10));
        film.setMpa(mpa);
        film.setGenres(genres);
        film.setDirectors(directors);

        film = filmStorage.createFilm(film);

        Director updateDirector = new Director();
        director.setId(1);
        Set<Director> updirectors = new HashSet<>();
        updirectors.add(updateDirector);
        Film filmUpdated = new Film();
        filmUpdated.setId(film.getId());
        filmUpdated.setName("updated");
        filmUpdated.setDescription("updatedDescription3");
        filmUpdated.setDuration(Duration.ofMinutes(33));
        filmUpdated.setReleaseDate(LocalDate.of(1994, 10, 13));
        filmUpdated.setDirectors(updirectors);

        Film result = filmStorage.updateFilm(filmUpdated);

        Assertions.assertEquals("updated", result.getName());
        Assertions.assertEquals("updatedDescription3", result.getDescription());
        Assertions.assertEquals(33, result.getDuration().toMinutes());
        Assertions.assertEquals(film.getId(), result.getId());
    }
     */
}

