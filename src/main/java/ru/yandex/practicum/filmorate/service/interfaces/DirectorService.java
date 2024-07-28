package ru.yandex.practicum.filmorate.service.interfaces;

import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.director.NewDirectorRequest;
import ru.yandex.practicum.filmorate.dto.director.UpdateDirectorRequest;

import java.util.Collection;

public interface DirectorService {
    Collection<DirectorDto> getAllDirectors();

    DirectorDto findDirectorById(Integer id);

    DirectorDto addDirector(NewDirectorRequest request);

    DirectorDto updateDirector(UpdateDirectorRequest request);

    void deleteDirector(int id);

}
