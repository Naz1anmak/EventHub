package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UserMetadataCreateDto(

        @NotNull(message = "UserId field must not be null")
        UUID userId,

        String firstName,
        String lastName,
        String bio,

        @Size(max = 15, message = "Phone number must not exceed 15 characters")
        String phone
) {
}
