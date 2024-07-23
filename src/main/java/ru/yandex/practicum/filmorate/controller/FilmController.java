package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<FilmDto> findAllFilms() {
        log.info("Получен запрос на вывод всех фильмов");
        return filmService.getAllFilms();
    }

    @PostMapping
    public FilmDto createFilm(@RequestBody NewFilmRequest request) {
        log.info("Получен запрос на создание фильма");
        return filmService.createFilm(request);
    }

    @PutMapping
    public FilmDto updateFilm(@RequestBody UpdateFilmRequest request) {
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
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(required = false) Integer year,
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
}
