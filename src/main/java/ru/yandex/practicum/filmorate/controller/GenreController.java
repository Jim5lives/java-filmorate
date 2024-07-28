package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.service.interfaces.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@Slf4j
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public Collection<GenreDto> getAllGenres() {
        log.info("Получен запрос на вывод всех жанров");
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public GenreDto findGenreById(@PathVariable Integer id) {
        log.info("Получен запрос на вывод жанра id");
        return genreService.findGenreById(id);
    }

}
