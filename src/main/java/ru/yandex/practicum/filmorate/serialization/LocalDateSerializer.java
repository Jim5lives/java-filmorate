package ru.yandex.practicum.filmorate.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer extends JsonSerializer<LocalDate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        String dateString = localDate.format(FORMATTER);
        jsonGenerator.writeString(dateString);
    }
}