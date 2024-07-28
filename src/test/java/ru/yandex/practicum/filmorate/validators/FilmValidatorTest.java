package ru.yandex.practicum.filmorate.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

class FilmValidatorTest {

    @Test
    void isFilmInfoValid_shouldReturnTrueWhenFieldsAreCorrect() {
        Film film = new Film(0, "Film", "Description",
                LocalDate.of(2000, 1, 1), 30);

        boolean isFilmValid = FilmValidator.isFilmInfoValid(film);

        Assertions.assertTrue(isFilmValid);
    }

    @Test
    void isFilmInfoValid_shouldThrowValidationExceptionWhen_nameIsNull() {
        Film film = new Film(0, null, "Description", null,
                LocalDate.of(2000, 1, 1), null, null);

        Assertions.assertThrows(ValidationException.class, () -> FilmValidator.isFilmInfoValid(film));
    }

    @Test
    void isFilmInfoValid_shouldThrowValidationExceptionWhen_nameIsBlank() {
        Film film = new Film(0, "", "Description", null,
                LocalDate.of(2000, 1, 1), null, null);

        Assertions.assertThrows(ValidationException.class, () -> FilmValidator.isFilmInfoValid(film));
    }

    @Test
    void isFilmInfoValid_shouldThrowValidationExceptionWhen_descriptionLengthMoreThan200() {
        Film film = new Film(0, "Film", "keioeskqtamrocnfsemkgollhfdifjldcvegbonuvcceqfxlasj" +
                "jhvgksasdcibfagqzsdgwmyrcklnswjttuveparavwucnbcwtnjucedtxawpqbafydgdqiamtlmuuvfaqffvmpwedgqyuwpncx" +
                "rcjzzutnjyahzqzkpbswglvtmkktpemxauqkklpnk21231fnjplvnvmj", null,
                LocalDate.of(2000, 1, 1), null, null);

        Assertions.assertThrows(ValidationException.class, () -> FilmValidator.isFilmInfoValid(film));
    }

    @Test
    void isFilmInfoValid_shouldThrowValidationExceptionWhen_releaseDateIsBefore_1895_12_28() {
        Film film = new Film(0, "Film", "Description", null,
                LocalDate.of(1895, 12, 27), null, null);

        Assertions.assertThrows(ValidationException.class, () -> FilmValidator.isFilmInfoValid(film));
    }

    @Test
    void isFilmInfoValid_shouldThrowValidationExceptionWhen_DurationIsNegative() {
        Film film = new Film(0, "Film", "Description",
                LocalDate.of(1895, 12, 27), -1);

        Assertions.assertThrows(ValidationException.class, () -> FilmValidator.isFilmInfoValid(film));

    }
}
