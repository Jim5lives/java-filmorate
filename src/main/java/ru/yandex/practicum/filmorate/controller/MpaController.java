package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.service.interfaces.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public Collection<MpaDto> getAllMpa() {
        log.info("Получен запрос на вывод всех возрастных рейтингов");
        return mpaService.getAllMpa();
    }

    @GetMapping("/{id}")
    public MpaDto findMpaById(@PathVariable Integer id) {
        log.info("Получен запрос на вывод возрастного рейтинга по id");
        return mpaService.findMpaById(id);
    }

}
