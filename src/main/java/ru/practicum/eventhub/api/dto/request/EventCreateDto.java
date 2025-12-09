package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

public record EventCreateDto(

        @NotBlank(message = "Title must not be blank")
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        String title,

        @Size(max = 255, message = "Description must be up to 255 characters")
        String description,

        @FutureOrPresent(message = "Event date must be in the present or future")
        OffsetDateTime eventDate,

        String location,

        @NotNull(message = "CategoryId field must not be null")
        UUID categoryId,

        @NotNull(message = "CreatedBy field must not be null")
        UUID createdBy,

        Set<UUID> tags
) {
}
