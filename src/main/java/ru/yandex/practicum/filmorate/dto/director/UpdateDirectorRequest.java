package ru.yandex.practicum.filmorate.dto.director;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateDirectorRequest {
    private Integer id;
    @Size(max = 100, message = "Имя режиссера не должно быть длиннее 100 символов")
    private String name;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }
}



