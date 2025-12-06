package ru.practicum.eventhub.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProjectDto(
        UUID id,
        String name,
        String description,
        CategoryShortDto category,
        UserShortDto owner
) {
}
