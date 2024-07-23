package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {
    private Integer id;
    private String name;
    private String description;
    private Set<Integer> likes;
    private LocalDate releaseDate;
    private Duration duration;
    private Set<Genre> genres;
    private Mpa mpa;
    private Set<Director> directors;

    public Film(Integer id, String name, String description,
                Duration duration, LocalDate releaseDate, Set<Genre> genres, Set<Director> directors) {
        this.id = id;
        this.name = name;
        this.likes = new HashSet<>();
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.directors = directors;
    }

    public Film(Integer id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.likes = new HashSet<>();
        this.releaseDate = releaseDate;
        this.duration = Duration.ofMinutes(duration);
    }

    public Film() {

    }

    public void addUserIdToFilmLikes(Integer id) {
        likes.add(id);
    }

    public void deleteUserIdFromFilmLikes(Integer id) {
        likes.remove(id);
    }
}


