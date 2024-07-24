package ru.yandex.practicum.filmorate.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {

    public static Film mapTofilm(NewFilmRequest request) {
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());

        if (request.getGenres() != null && !request.getGenres().isEmpty()) {
            Set<Genre> genres = request.getGenres().stream()
                    .map(GenreMapper::mapToGenre)
                    .collect(Collectors.toSet());
            film.setGenres(genres);
        } else {
            film.setGenres(new HashSet<>());
        }

        if (null != request.getMpa()) {
            film.setMpa(MpaMapper.mapToMpa(request.getMpa()));
        } else {
            film.setMpa(new Mpa());
        }

        if (request.getDirectors() != null && !request.getDirectors().isEmpty()) {
            Set<Director> directors = request.getDirectors().stream()
                    .map(DirectorMapper::mapToDirector)
                    .collect(Collectors.toSet());
            film.setDirectors(directors);
        } else {
            film.setDirectors(new HashSet<>());
        }

        return film;
    }

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setLikes(film.getLikes());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());

        Set<GenreDto> genresDto = film.getGenres().stream()
                .map(GenreMapper::mapToGenreDto)
                .collect(Collectors.toSet());
        dto.setGenres(genresDto);

        dto.setMpa(MpaMapper.mapToMpaDto(film.getMpa()));

        Set<DirectorDto> directorsDto = film.getDirectors().stream()
                .map(DirectorMapper::mapToDirectorDto)
                .collect(Collectors.toSet());
        dto.setDirectors(directorsDto);
        return dto;
    }

    public static Film updateFilmFields(Film film, UpdateFilmRequest request) {
        if (request.hasName()) {
            film.setName(request.getName());
        }
        if (request.hasDescription()) {
            film.setDescription(request.getDescription());
        }
        if (request.hasReleaseDate()) {
            film.setReleaseDate(request.getReleaseDate());
        }
        if (request.hasDuration()) {
            film.setDuration(request.getDuration());
        }

        if (request.hasMpa()) {
            film.setMpa(request.getMpa());
        }

        if (request.hasGenres()) {
            film.setGenres(request.getGenres());
        }

        if (request.hasDirectors()) {
            film.setDirectors(request.getDirectors());
        } else {
            film.setDirectors(new HashSet<>());
        }

        return film;
    }
}
