package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.yandex.practicum.filmorate.serialization.DurationSerializer;
import ru.yandex.practicum.filmorate.serialization.LocalDateSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    private String name;
    private String description;
    private Set<Integer> likes;
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate releaseDate;
    @JsonSerialize(using = DurationSerializer.class)
    private Duration duration;
    private Set<GenreDto> genres;
    private MpaDto mpa;
    private Set<DirectorDto> directors;

}
