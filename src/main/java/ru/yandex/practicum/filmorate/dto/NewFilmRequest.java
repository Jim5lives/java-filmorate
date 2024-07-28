package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.controller.DurationConstraint;
import ru.yandex.practicum.filmorate.controller.ReleaseDateConstraint;
import ru.yandex.practicum.filmorate.serialization.DurationDeserializer;
import ru.yandex.practicum.filmorate.serialization.LocalDateDeserializer;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
@Validated
public class NewFilmRequest {
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message
            = "Name must be < 50 characters")
    private String name;

    @NotBlank
    @Size(max = 200, message
            = "Description must be between 0 and 200 characters")
    private String description;

    @ReleaseDateConstraint
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate releaseDate;

    @DurationConstraint
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration duration;

    private Set<GenreDto> genres;

    @NotNull
    private MpaDto mpa;

    private Set<DirectorDto> directors;

}
