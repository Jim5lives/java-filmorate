package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.director.NewDirectorRequest;
import ru.yandex.practicum.filmorate.dto.director.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.service.interfaces.DirectorService;

import java.util.Collection;

@RestController
@RequestMapping("/directors")
@Slf4j
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public Collection<DirectorDto> getAllDirectors() {
        log.info("Получен запрос на вывод всех режиссеров");
        return directorService.getAllDirectors();
    }

    @GetMapping("/{id}")
    public DirectorDto findDirectorById(@PathVariable Integer id) {
        log.info("Получен запрос на вывод режиссера по id");
        return directorService.findDirectorById(id);
    }

    @PostMapping
    public DirectorDto addDirector(@Valid @RequestBody NewDirectorRequest request) {
        log.info("Получен запрос на создание режиссера");
        return directorService.addDirector(request);
    }

    @PutMapping
    public DirectorDto updateDirector(@Valid @RequestBody UpdateDirectorRequest request) {
        log.info("Получен запрос на обновление режиссера");
        return directorService.updateDirector(request);
    }

    @DeleteMapping("/{id}")
    public void deleteDirector(@PathVariable int id) {
        log.info("Получен запрос на удаление режиссера");
        directorService.deleteDirector(id);
    }
}
