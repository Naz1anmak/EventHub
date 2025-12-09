package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.Size;

public record UserMetadataCreateDto(

        String firstName,
        String lastName,
        String bio,

        @Size(max = 15, message = "Phone number must not exceed 15 characters")
        String phone
) {
}
