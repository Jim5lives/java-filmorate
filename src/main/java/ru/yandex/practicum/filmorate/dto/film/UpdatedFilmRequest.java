package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.validators.DurationConstraint;
import ru.yandex.practicum.filmorate.validators.ReleaseDateConstraint;
import ru.yandex.practicum.filmorate.serialization.DurationSerializer;
import ru.yandex.practicum.filmorate.serialization.LocalDateDeserializer;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
@Validated
public class UpdatedFilmRequest {
    @Positive
    private Integer id;

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
    @JsonSerialize(using = DurationSerializer.class)
    private Duration duration;

    private Set<GenreDto> genres;

    @NotNull
    private MpaDto mpa;
    private Set<DirectorDto> directors;
}
