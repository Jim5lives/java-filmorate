package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator {

    private FilmValidator() {
    }

    public static final int MAX_DESCRIPTION_LENGTH = 200;
    public static final LocalDate THE_BIRTH_OF_CINEMA = LocalDate.of(1895, 12, 28);

    public static boolean isFilmInfoValid(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");

        } else if (film.getDescription() == null || film.getDescription().isBlank()) {
            throw new ValidationException("Описание фильма не может быть пустым.");

        } else if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("Максимальная длина описания — 200 символов.");

        } else if (film.getReleaseDate() == null) {
            throw new ValidationException("Дата релиза не может пустой.");

        } else if (film.getReleaseDate().isBefore(THE_BIRTH_OF_CINEMA)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");

        } else if (film.getDuration() == null) {
            throw new ValidationException("Продолжительность фильма не может быть пустой");

        } else if (film.getDuration().isNegative()) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }

        return true;
    }
}
