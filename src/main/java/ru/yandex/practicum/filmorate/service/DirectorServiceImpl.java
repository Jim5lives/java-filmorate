package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectorServiceImpl implements DirectorService {
    private final DirectorStorage directorStorage;

    @Override
    public Collection<DirectorDto> getAllDirectors() {
        return directorStorage.getAllDirectors().stream()
                .map(DirectorMapper::mapToDirectorDto)
                .toList();
    }

    @Override
    public DirectorDto findDirectorById(Integer id) {
        return directorStorage.findDirectorById(id)
                .map(DirectorMapper::mapToDirectorDto)
                .orElseThrow(() -> new NotFoundException("Не найден жанр с ID: " + id));
    }

    @Override
    public DirectorDto addDirector(DirectorDto directorDto) {
        Director director = DirectorMapper.mapToDirector(directorDto);
        Integer id = directorStorage.addDirector(director).getId();
        directorDto.setId(id);
        return directorDto;
    }

    @Override
    public DirectorDto updateDirector(DirectorDto directorDto) {
        int id = directorDto.getId();
        Director dir = directorStorage.findDirectorById(id).orElseThrow(()
                -> new NotFoundException("Не найден режиссер с ID: " + id));
        Director director = DirectorMapper.mapToDirector(directorDto);
        Director updatedDirector = directorStorage.updateDirector(director);

        return DirectorMapper.mapToDirectorDto(updatedDirector);
    }

    @Override
    public void deleteDirector(int id) {
        directorStorage.deleteDirector(id);
    }
}
