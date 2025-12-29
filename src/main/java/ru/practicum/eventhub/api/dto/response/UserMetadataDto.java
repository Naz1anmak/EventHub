package ru.practicum.eventhub.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserMetadataDto(
        UUID id,
        UserShortDto user,
        String firstName,
        String lastName,
        String address,
        String phone,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
