package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import ru.yandex.practicum.filmorate.serialization.DurationDeserializer;
import ru.yandex.practicum.filmorate.serialization.LocalDateDeserializer;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
public class NewFilmRequest {
    private String name;
    private String description;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate releaseDate;
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration duration;
    private Set<GenreDto> genres;
    private MpaDto mpa;
    private Set<DirectorDto> directors;

}
