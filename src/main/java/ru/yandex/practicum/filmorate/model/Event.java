package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class Event {
    private int id;
    private int userId;
    private int entityId;
    private Timestamp timestamp;
    private String eventType;
    private String operation;
}
