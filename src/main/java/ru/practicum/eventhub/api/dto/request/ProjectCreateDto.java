package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ProjectCreateDto(

        @NotBlank(message = "Name must not be blank")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        @Size(max = 255, message = "Description must be up to 255 characters")
        String description,

        UUID categoryId,

        @NotNull(message = "OwnerId must not be null")
        UUID ownerId
) {
}
