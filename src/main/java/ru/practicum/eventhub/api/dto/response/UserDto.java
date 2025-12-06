package ru.practicum.eventhub.api.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email,
        OffsetDateTime createdAt
) {
}
