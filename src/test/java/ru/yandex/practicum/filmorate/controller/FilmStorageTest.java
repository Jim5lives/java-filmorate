package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmStorageTest {
    InMemoryFilmStorage filmStorage;

    @BeforeEach
    void beforeEach() {
        filmStorage = new InMemoryFilmStorage();
    }

    @Test
    void findAllFilms_shouldReturnAllFilms() {
        Film film1 = new Film(0, "Film1", "Description1",
                LocalDate.of(2000, 1, 1), 100);
        Film film2 = new Film(0, "Film2", "Description2",
                LocalDate.of(2000, 1, 1), 100);
        filmStorage.createFilm(film1);
        filmStorage.createFilm(film2);

        Collection<Film> allFilms = filmStorage.getAllFilms();

        assertEquals(2, allFilms.size());
    }

    @Test
    void createFilm_shouldAddNewFilm() {
        Film film = new Film(0, "Film", "Description",
                LocalDate.of(2000, 1, 1), 100);

        Film createdFilm = filmStorage.createFilm(film);

        assertEquals(1, filmStorage.getAllFilms().size());
        assertEquals(film.getName(), createdFilm.getName());
    }

    @Test
    void createFilm_shouldSetIdInOrder() {
        Film film1 = new Film(0, "Film1", "Description1",
                LocalDate.of(2000, 1, 1), 100);
        Film film2 = new Film(0, "Film2", "Description2",
                LocalDate.of(2000, 1, 1), 100);
        Film film3 = new Film(0, "Film3", "Description3",
                LocalDate.of(2000, 1, 1), 100);

        Film createdFilm1 = filmStorage.createFilm(film1);
        Film createdFilm2 = filmStorage.createFilm(film2);
        Film createdFilm3 = filmStorage.createFilm(film3);

        assertEquals(3, filmStorage.getAllFilms().size());
        assertEquals(1, createdFilm1.getId());
        assertEquals(2, createdFilm2.getId());
        assertEquals(3, createdFilm3.getId());
    }

    @Test
    void updateFilm_shouldUpdateFilmWhenAllFieldsAreCorrect() {
        Film film = new Film(1, "Film", "Description",
                LocalDate.of(2000, 1, 1), 100);
        filmStorage.createFilm(film);
        Film updatedFilm = new Film(1, "FilmUpdated", "DescriptionUpdated",
                LocalDate.of(2005, 5, 5), 50);

        filmStorage.updateFilm(updatedFilm);

        List<Film> filmsList = new ArrayList<>(filmStorage.getAllFilms());
        Film updatedActual = filmsList.getFirst();
        assertEquals("FilmUpdated", updatedActual.getName());
        assertEquals("DescriptionUpdated", updatedActual.getDescription());
        assertEquals(LocalDate.of(2005, 5, 5), updatedActual.getReleaseDate());
        assertEquals(50, updatedActual.getDuration().toMinutes());
    }

    @Test
    void updateFilm_shouldThrowValidationExceptionWhenIdIsNull() {
        Film film = new Film(1, "Film", "Description",
                LocalDate.of(2000, 1, 1), 100);
        filmStorage.createFilm(film);
        Film updatedFilm = new Film(null, "FilmUpdated", "DescriptionUpdated",
                LocalDate.of(2005, 5, 5), 50);

        Assertions.assertThrows(ValidationException.class, () -> filmStorage.updateFilm(updatedFilm));
    }

    @Test
    void updateFilm_shouldThrowNotFoundExceptionWhenIdNonExistent() {
        Film film = new Film(1, "Film", "Description",
                LocalDate.of(2000, 1, 1), 100);
        filmStorage.createFilm(film);
        Film updatedFilm = new Film(7, "FilmUpdated", "DescriptionUpdated",
                LocalDate.of(2005, 5, 5), 50);

        Assertions.assertThrows(NotFoundException.class, () -> filmStorage.updateFilm(updatedFilm));
    }
}
