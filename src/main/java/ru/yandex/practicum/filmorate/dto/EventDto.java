package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventDto {
    int userId;
    int entityId;
    long timestamp;
    String eventType;
    String operation;
}
