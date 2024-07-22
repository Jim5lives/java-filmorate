package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class Event {
    int id;
    int userId;
    int entityId;
    Timestamp timestamp;
    String eventType;
    String operation;
}
