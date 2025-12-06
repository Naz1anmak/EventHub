package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ProjectUpdateDto(

        @Size(max = 100, message = "Name must be up to 100 characters")
        String name,

        @Size(max = 255, message = "Description must be up to 255 characters")
        String description,

        UUID categoryId,

        UUID ownerId
) {
}
