package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.Size;

public record CategoryCreateDto(

        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        @Size(max = 255, message = "Description must be up to 255 characters")
        String description
) {
}
