package ru.practicum.eventhub.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(
        UUID id,
        String username,
        String email,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
