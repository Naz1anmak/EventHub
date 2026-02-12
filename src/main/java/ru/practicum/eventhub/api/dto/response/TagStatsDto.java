package ru.practicum.eventhub.api.dto.response;

import java.time.OffsetDateTime;

public record TagStatsDto(
        long usageCount,
        OffsetDateTime lastUsedAt
) {
}
