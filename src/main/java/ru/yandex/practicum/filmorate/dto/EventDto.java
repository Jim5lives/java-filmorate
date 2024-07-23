package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventDto {
    private int userId;
    private int entityId;
    private long timestamp;
    private String eventType;
    private String operation;
}
