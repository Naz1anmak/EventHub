package ru.practicum.eventhub.api.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TagStatsDto(
        UUID tagId,
        long usageCount,
        OffsetDateTime lastUsedAt
) {
}
