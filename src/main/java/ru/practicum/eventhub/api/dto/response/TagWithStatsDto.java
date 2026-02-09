package ru.practicum.eventhub.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.practicum.eventhub.api.model.EventShortDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TagWithStatsDto(
        UUID id,
        String name,
        String description,
        List<EventShortDto> events,
        Long usageCount,
        OffsetDateTime lastUsedAt
) {
}
