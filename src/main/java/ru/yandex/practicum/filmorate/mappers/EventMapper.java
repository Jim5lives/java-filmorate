package ru.yandex.practicum.filmorate.mappers;

import ru.yandex.practicum.filmorate.dto.EventDto;
import ru.yandex.practicum.filmorate.model.Event;

public class EventMapper {
    public static EventDto mapToDto(Event event) {
        return EventDto.builder()
                .eventId(event.getId())
                .userId(event.getUserId())
                .entityId(event.getEntityId())
                .operation(event.getOperation())
                .eventType(event.getEventType())
                .timestamp(event.getTimestamp().getTime()).build();
    }
}
