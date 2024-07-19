package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmService {

    FilmDto findFilmById(Integer id);

    Collection<FilmDto> getAllFilms();

    FilmDto createFilm(NewFilmRequest request);

    FilmDto updateFilm(UpdateFilmRequest updatedFilm);

    Collection<FilmDto> getPopularFilms(int count,Integer year, Integer genreId);

    FilmDto addLike(Integer filmId, Integer userId);

    FilmDto deleteLike(Integer filmId, Integer userId);
}
