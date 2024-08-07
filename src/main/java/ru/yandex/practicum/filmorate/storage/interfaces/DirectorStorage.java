package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DirectorStorage {
    Collection<Director> getAllDirectors();

    Optional<Director> findDirectorById(Integer id);

    Director addDirector(Director director);

    Director updateDirector(Director director);

    void deleteDirector(int id);

    int countExistingDirectorsFromList(List<Integer> directorsInQuestion);
}
