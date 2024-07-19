package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.service.DirectorService;

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
    public DirectorDto addDirector(@RequestBody DirectorDto directorDto) {
        log.info("Получен запрос на создание режиссера");
        return directorService.addDirector(directorDto);
    }

    @PutMapping
    public DirectorDto updateDirector(@RequestBody DirectorDto directorDto) {
        log.info("Получен запрос на обновление режиссера");
        return directorService.updateDirector(directorDto);
    }

    @DeleteMapping("/{id}")
    public void deleteDirector(@PathVariable int id) {
        log.info("Получен запрос на удаление режиссера");
        directorService.deleteDirector(id);
    }
}
