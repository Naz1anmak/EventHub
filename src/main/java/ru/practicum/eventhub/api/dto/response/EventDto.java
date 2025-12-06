package ru.practicum.eventhub.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EventDto(
        UUID id,
        String title,
        String description,
        OffsetDateTime eventDate,
        String location,
        CategoryShortDto category,
        UserShortDto createdBy,
        Set<TagShortDto> tags
) {
    public record TagShortDto(UUID id, String name) {
    }
}
