package ru.yandex.practicum.filmorate.service.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.director.NewDirectorRequest;
import ru.yandex.practicum.filmorate.dto.director.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mappers.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.interfaces.DirectorService;
import ru.yandex.practicum.filmorate.storage.interfaces.DirectorStorage;

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
                .orElseThrow(() -> new NotFoundException("Не найден режиссер с ID: " + id));
    }

    @Override
    public DirectorDto addDirector(NewDirectorRequest request) {
        Director director = DirectorMapper.mapToDirector(request);
        if (director.getName().isBlank()) {
            throw new ValidationException("Некорректное имя режиссера");
        }
        director = directorStorage.addDirector(director);
        log.info("Режиссер успешно создан, ID = {}", director.getId());
        return DirectorMapper.mapToDirectorDto(director);
    }

    @Override
    public DirectorDto updateDirector(UpdateDirectorRequest request) {
        if (request.getId() == null) {
            log.warn("Не указан id режиссера для обновления");
            throw new ValidationException("Id режиссера для обновления должен быть указан.");
        }

        Director directorToUpdate = directorStorage.findDirectorById(request.getId()).orElseThrow(()
                -> new NotFoundException("Не найден режиссер с ID: " + request.getId()));

        Director updatedDirector = DirectorMapper.updateDirectorFields(directorToUpdate, request);
        updatedDirector = directorStorage.updateDirector(updatedDirector);

        log.info("Режиссер обновлен {}", updatedDirector);
        return DirectorMapper.mapToDirectorDto(updatedDirector);
    }

    @Override
    public void deleteDirector(int id) {
        directorStorage.deleteDirector(id);
        log.info("Режиссер с ID = {} удален", id);
    }
}
