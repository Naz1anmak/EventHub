package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.Size;

public record TagCreateDto(

        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,

        @Size(max = 255, message = "Description must be up to 255 characters")
        String description
) {
}
