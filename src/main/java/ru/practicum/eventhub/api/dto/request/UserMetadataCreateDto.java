package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UserMetadataCreateDto(

        @NotBlank(message = "UserId field must not be blank")
        UUID userId,

        String firstName,
        String lastName,
        String bio,

        @Size(max = 15, message = "Phone number must not exceed 15 characters")
        String phone
) {
}
