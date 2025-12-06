package ru.practicum.eventhub.api.dto.response;

import java.util.UUID;

public record UserShortDto(
        UUID id,
        String username,
        String email
) {
}
