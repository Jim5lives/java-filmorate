package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.dto.NewDirectorRequest;
import ru.yandex.practicum.filmorate.dto.UpdateDirectorRequest;

import java.util.Collection;

public interface DirectorService {
    Collection<DirectorDto> getAllDirectors();

    DirectorDto findDirectorById(Integer id);

    DirectorDto addDirector(NewDirectorRequest request);

    DirectorDto updateDirector(UpdateDirectorRequest request);

    void deleteDirector(int id);

}
