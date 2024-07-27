package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    Collection<Genre> getAllGenres();

    Optional<Genre> findGenreById(Integer id);

    int countExistingGenresFromList(List<Integer> genresInQuestion);
}
