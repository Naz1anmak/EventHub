package ru.practicum.eventhub.api.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TagDto(
        UUID id,
        String name,
        String description,
        Set<EventShortDto> events
) {
    public record EventShortDto(UUID id, String title) {
    }
}
