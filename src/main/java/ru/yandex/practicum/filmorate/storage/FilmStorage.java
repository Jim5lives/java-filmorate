package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Collection<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film updatedFilm);

    Optional<Film> findFilmById(Integer id);

    Film addLike(Film film, Integer userId);

    Film deleteLike(Film film, Integer userId);

    Collection<Film> getPopularFilms(int count, Integer year, Integer genreId);

    List<Film> getFilmsByDirector(String sortBy, Integer directorId);
  
    Collection<Film> getCommonFilms(Integer userId, Integer friendId);
}
