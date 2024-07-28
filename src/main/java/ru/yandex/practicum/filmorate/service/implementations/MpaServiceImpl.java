package ru.yandex.practicum.filmorate.service.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.MpaMapper;
import ru.yandex.practicum.filmorate.service.interfaces.MpaService;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaServiceImpl implements MpaService {
    private final MpaStorage mpaStorage;

    @Override
    public Collection<MpaDto> getAllMpa() {
        return mpaStorage.getAllMpa().stream()
                .map(MpaMapper::mapToMpaDto)
                .toList();
    }

    @Override
    public MpaDto findMpaById(Integer id) {
        return mpaStorage.findMpaById(id)
                .map(MpaMapper::mapToMpaDto)
                .orElseThrow(() -> new NotFoundException("Не найден возрастной рейтинг с ID: " + id));
    }
}
