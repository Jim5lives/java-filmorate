package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final DirectorStorage directorStorage;


    @Override
    public FilmDto findFilmById(Integer id) {
        Film film = filmStorage.findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Не найден фильм с ID:"));
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public Collection<FilmDto> getAllFilms() {
        filmStorage.getAllFilms();
        return filmStorage.getAllFilms().stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    @Override
    public FilmDto createFilm(NewFilmRequest request) {
        validate(request);
        Film film = filmStorage.createFilm(FilmMapper.mapTofilm(request));
        FilmDto filmDto = FilmMapper.mapToFilmDto(film);
        log.info("Фильм создан: {}", filmDto);
        return filmDto;
    }

    @Override
    public FilmDto updateFilm(UpdatedFilmRequest request) {
        validate(request);
        Film updatedFilm = FilmMapper.mapTofilm(request);
        filmStorage.updateFilm(updatedFilm);
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
    public List<FilmDto> getFilmsByDirector(String sortBy, Integer directorId) {
        if (directorId != null) {
            boolean directorExists = directorStorage.getAllDirectors().stream()
                    .map(Director::getId)
                    .anyMatch(id -> id.equals(directorId));
            if (!directorExists) {
                throw new NotFoundException("Не существует режиссера с ID: " + directorId);
            }
        }

        log.info("Выводится список фильмов режиссера отсортированных по количеству лайков или году выпуска");
        return filmStorage.getFilmsByDirector(sortBy, directorId).stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
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

    @Override
    public void deleteFilmById(int id) {
        filmStorage.findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм не найден с ID: " + id));
        filmStorage.deleteFilmById(id);
    }

    @Override
    public Collection<FilmDto> search(String query, String by) {
        Collection<Film> films = filmStorage.search(query, by);
        log.info("Выводится список фильмов, содержащих {} по {}", query, by);
        return films.stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingInt(FilmDto::getId).reversed())));
    }

    private void validate(NewFilmRequest request) {
        validate(request.getMpa(), request.getGenres(), request.getDirectors());
    }

    private void validate(UpdatedFilmRequest request) {
        Film existingFilm = filmStorage.findFilmById(request.getId())
                .orElseThrow(() -> new NotFoundException("Фильм с указанным id = " + request.getId() + " не найден."));
        validate(request.getMpa(), request.getGenres(), request.getDirectors());
    }

    private void validate(MpaDto mpaDto, Set<GenreDto> genreDtos, Set<DirectorDto> directorDtos) {
        Integer mpaId = mpaDto.getId();
        Mpa mpa = mpaStorage.findMpaById(mpaId)
                .orElseThrow(() -> new ValidationException("Не существует рейтинга с ID:" + mpaId));

        if (genreDtos != null) {
            List<Integer> genreIds = genreDtos.stream()
                    .map(GenreDto::getId)
                    .toList();
            int numOfExistingGenres = genreStorage.countExistingGenresFromList(genreIds);
            if (genreIds.size() != numOfExistingGenres) {
                throw new ValidationException("Передан несуществующий жанр");
            }
        }

        if (directorDtos != null) {
            List<Integer> directorIds = directorDtos.stream()
                    .map(DirectorDto::getId)
                    .toList();
            int numOfExistingDirectors = directorStorage.countExistingDirectorsFromList(directorIds);

            if (directorIds.size() != numOfExistingDirectors) {
                throw new ValidationException("Передан несуществующий режиссер");
            }
        }
    }


}
