package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.Size;

public record UserMetadataUpdateDto(
        String firstName,
        String lastName,
        String bio,

        @Size(max = 15, message = "Номер телефона должен быть до 15 символов")
        String phone
) {
}
