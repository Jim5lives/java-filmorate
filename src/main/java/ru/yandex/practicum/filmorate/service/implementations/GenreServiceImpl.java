package ru.yandex.practicum.filmorate.service.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.service.interfaces.GenreService;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {
    private final GenreStorage genreStorage;

    @Override
    public Collection<GenreDto> getAllGenres() {
        return genreStorage.getAllGenres().stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
    }

    @Override
    public GenreDto findGenreById(Integer id) {
        return genreStorage.findGenreById(id)
                .map(GenreMapper::mapToGenreDto)
                .orElseThrow(() -> new NotFoundException("Не найден жанр с ID: " + id));
    }
}
