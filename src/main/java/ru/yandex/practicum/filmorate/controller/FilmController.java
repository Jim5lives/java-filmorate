package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdatedFilmRequest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
@Validated
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<FilmDto> findAllFilms() {
        log.info("Получен запрос на вывод всех фильмов");
        return filmService.getAllFilms();
    }

    @PostMapping
    public FilmDto createFilm(@RequestBody @Valid NewFilmRequest request) {
        log.info("Получен запрос на создание фильма");
        return filmService.createFilm(request);
    }

    @PutMapping
    public FilmDto updateFilm(@RequestBody @Valid UpdatedFilmRequest request) {
        log.info("Получен запрос на обновление фильма");
        return filmService.updateFilm(request);
    }

    @GetMapping("/{id}")
    public FilmDto getFilm(@PathVariable Integer id) {
        log.info("Получен запрос на вывод фильма по id={}", id);
        return filmService.findFilmById(id);
    }

    @GetMapping("/popular")
    public Collection<FilmDto> getPopularFilms(
            @RequestParam(defaultValue = "10") @Positive int count,
            @RequestParam(required = false) @Min(value = 1895, message = "Первый фильм вышел в 1895 году") Integer year,
            @RequestParam(required = false) Integer genreId) {
        log.info("Получен запрос на вывод популярных фильмов");
        return filmService.getPopularFilms(count, year, genreId);
    }

    @PutMapping("/{id}/like/{userId}")
    public FilmDto addLike(@PathVariable("id") Integer filmId,
                           @PathVariable Integer userId) {
        log.info("Получен запрос на добавление лайка фильму с id={} от пользователя с id={}", filmId, userId);
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public FilmDto deleteLike(@PathVariable("id") Integer filmId,
                              @PathVariable Integer userId) {
        log.info("Получен запрос на удаление лайка у фильма с id={} от пользователя с id={}", filmId, userId);
        return filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/director/{directorId}")
    public List<FilmDto> getFilmsByDirector(@RequestParam String sortBy, @PathVariable int directorId) {
        String sortingCriteria;
        try {
            sortingCriteria = SortingCriteria.valueOf(sortBy.toUpperCase()).toString();
        } catch (IllegalArgumentException iae) {
            throw new ValidationException("Неправильно выбран параметр sortBу");
        }
        return filmService.getFilmsByDirector(sortingCriteria, directorId);
    }

    @GetMapping("/common")
    public Collection<FilmDto> getCommonFilms(@RequestParam Integer userId, @RequestParam Integer friendId) {
        log.info("Получен запрос на вывод общих фильмов у пользователя id={} и пользователя с id={}", userId, friendId);
        return filmService.getCommonFilms(userId, friendId);
    }

    @DeleteMapping("{id}")
    public void deleteFilmById(@PathVariable int id) {
        log.info("Получен запрос на удаление фильма с id={}", id);
        filmService.deleteFilmById(id);
    }

    @GetMapping("/search")
    public Collection<FilmDto> search(@RequestParam String query,
                                      @RequestParam String by) {
        log.info("Получен запрос на поиск фильмов по подстроке {}", query);
        return filmService.search(query, by);
    }
}
