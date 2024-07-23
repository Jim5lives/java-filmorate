package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.mappers.MpaMapper;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validators.FilmValidator.isFilmInfoValid;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Override
    public FilmDto findFilmById(Integer id) {
        Film film = filmStorage.findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Не найден фильм с ID:"));
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public Collection<FilmDto> getAllFilms() {
        return filmStorage.getAllFilms().stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    @Override
    public FilmDto createFilm(NewFilmRequest request) {
        Film film = FilmMapper.mapTofilm(request);
        // валидация
        try {
            isFilmInfoValid(film);
        } catch (ValidationException e) {
            log.warn("Ошибка валидации: {}", e.getMessage());
            throw new ValidationException(e.getMessage());
        }
        Integer mpaId = film.getMpa().getId();
        Mpa mpa = mpaStorage.findMpaById(mpaId)
                .orElseThrow(() -> new ValidationException("Не существует рейтинга с ID:" + mpaId));

        if (request.getGenres() != null) {
            List<Integer> genreIds = film.getGenres().stream()
                    .map(Genre::getId)
                    .toList();
            for (Integer id : genreIds) {
                genreStorage.findGenreById(id)
                        .orElseThrow(() -> new ValidationException("Не существует жанра с ID:" + id));
            }
        } else {
            film.setGenres(new HashSet<>());
        }
        film = filmStorage.createFilm(film);

        // маппинг
        FilmDto filmDto = FilmMapper.mapToFilmDto(film);

        filmDto.setMpa(MpaMapper.mapToMpaDto(mpa));

        if (request.getGenres() != null && !request.getGenres().isEmpty()) {
            Set<GenreDto> genres = film.getGenres().stream()
                    .map(Genre::getId)
                    .map(genreStorage::findGenreById)
                    .flatMap(Optional::stream)
                    .map(GenreMapper::mapToGenreDto)
                    .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingInt(GenreDto::getId))));
            filmDto.setGenres(genres);
        } else {
            filmDto.setGenres(new HashSet<>());
        }

        log.info("Фильм создан: {}", filmDto);
        return filmDto;
    }

    @Override
    public FilmDto updateFilm(UpdateFilmRequest request) {
        if (request.getId() == null) {
            log.warn("Не указан id фильма для обновления");
            throw new ValidationException("Id фильма для обновления должен быть указан.");
        }

        Optional<Film> filmOpt = filmStorage.findFilmById(request.getId());
        if (filmOpt.isEmpty()) {
            log.warn("Фильм с id {} не найден", request.getId());
            throw new NotFoundException("Фильм с указанным id = " + request.getId() + " не найден.");
        }

        Film filmToUpdate = filmOpt.get();
        Film updatedFilm = FilmMapper.updateFilmFields(filmToUpdate, request);

        try {
            isFilmInfoValid(updatedFilm);
        } catch (ValidationException exception) {
            log.warn(exception.getMessage());
            throw exception;
        }
        updatedFilm = filmStorage.updateFilm(updatedFilm);
        log.info("Обновленный фильм {} сохранен", updatedFilm);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    @Override
    public Collection<FilmDto> getPopularFilms(int count, Integer year, Integer genreId) {
        if (genreId != null) {
            boolean genreExists = genreStorage.getAllGenres().stream()
                    .map(Genre::getId)
                    .anyMatch(id -> id.equals(genreId));
            if (!genreExists) {
                throw new ValidationException("Не существует жанра с ID: " + genreId);
            }
        }

        log.info("Выводится список популярных фильмов");
        return filmStorage.getPopularFilms(count, year, genreId).stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }


    @Override
    public FilmDto addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Не найден фильм с ID:"));
        User user = userStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с ID: " + userId));
        filmStorage.addLike(film, user.getId());
        Film updatedLikesFilm = filmStorage.findFilmById(film.getId())
                .orElseThrow(() -> new NotFoundException("Не найден фильм с ID:"));
        log.info("Пользователь id={} поставил лайк фильму id={}", userId, updatedLikesFilm.getId());

        userStorage.addEvent(user.getId(), film.getId(), EventType.LIKE, OperationType.ADD);

        return FilmMapper.mapToFilmDto(updatedLikesFilm);
    }

    @Override
    public FilmDto deleteLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Не найден фильм с ID:"));
        User user = userStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с ID: " + userId));

        if (!film.getLikes().contains(userId)) {
            log.warn("Лайк пользователя с id={} не найден у фильма id={}", userId, filmId);
            throw new NotFoundException("Лайк пользователя с id=" + userId + " не найден у фильма id=" + filmId);
        }
        filmStorage.deleteLike(film, userId);
        log.info("Пользователь id={} убрал лайк с фильма id={}", user.getId(), filmId);

        userStorage.addEvent(user.getId(), film.getId(), EventType.LIKE, OperationType.REMOVE);

        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public Collection<FilmDto> getCommonFilms(Integer userId, Integer friendId) {
        userStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));
        userStorage.findUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + friendId));

        return filmStorage.getCommonFilms(userId, friendId)
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }
}
