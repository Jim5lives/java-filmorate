package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    void findAllFilms_shouldReturnAllFilms() {
        Film film1 = new Film(0, "Film1", "Description1",
                LocalDate.of(2000, 1, 1), 100);
        Film film2 = new Film(0, "Film2", "Description2",
                LocalDate.of(2000, 1, 1), 100);
        filmController.createFilm(film1);
        filmController.createFilm(film2);

        Collection<Film> allFilms = filmController.findAllFilms();

        assertEquals(2, allFilms.size());
    }

    @Test
    void createFilm_shouldAddNewFilm() {
        Film film = new Film(0, "Film", "Description",
                LocalDate.of(2000, 1, 1), 100);

        Film createdFilm = filmController.createFilm(film);

        assertEquals(1, filmController.findAllFilms().size());
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

        Film createdFilm1 = filmController.createFilm(film1);
        Film createdFilm2 = filmController.createFilm(film2);
        Film createdFilm3 = filmController.createFilm(film3);

        assertEquals(3, filmController.findAllFilms().size());
        assertEquals(1, createdFilm1.getId());
        assertEquals(2, createdFilm2.getId());
        assertEquals(3, createdFilm3.getId());
    }

    @Test
    void updateFilm_shouldUpdateFilmWhenAllFieldsAreCorrect() {
        Film film = new Film(1, "Film", "Description",
                LocalDate.of(2000, 1, 1), 100);
        filmController.createFilm(film);
        Film updatedFilm = new Film(1, "FilmUpdated", "DescriptionUpdated",
                LocalDate.of(2005, 5, 5), 50);

        filmController.updateFilm(updatedFilm);

        List<Film> filmsList = new ArrayList<>(filmController.findAllFilms());
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
        filmController.createFilm(film);
        Film updatedFilm = new Film(null, "FilmUpdated", "DescriptionUpdated",
                LocalDate.of(2005, 5, 5), 50);

        Assertions.assertThrows(ValidationException.class, () -> filmController.updateFilm(updatedFilm));
    }

    @Test
    void updateFilm_shouldThrowNotFoundExceptionWhenIdNonExistent() {
        Film film = new Film(1, "Film", "Description",
                LocalDate.of(2000, 1, 1), 100);
        filmController.createFilm(film);
        Film updatedFilm = new Film(7, "FilmUpdated", "DescriptionUpdated",
                LocalDate.of(2005, 5, 5), 50);

        Assertions.assertThrows(NotFoundException.class, () -> filmController.updateFilm(updatedFilm));
    }
}