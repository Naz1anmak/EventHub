package ru.practicum.eventhub.api.dto.response;

import java.util.UUID;

public record CategoryShortDto(
        UUID id,
        String name
) {
}
