package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.DirectorDto;

import java.util.Collection;

public interface DirectorService {
    Collection<DirectorDto> getAllDirectors();

    DirectorDto findDirectorById(Integer id);

    DirectorDto addDirector(DirectorDto directorDto);

    DirectorDto updateDirector(DirectorDto directorDto);

    void deleteDirector(int id);

}
