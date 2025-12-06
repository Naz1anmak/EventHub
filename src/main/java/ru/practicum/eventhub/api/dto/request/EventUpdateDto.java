package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

public record EventUpdateDto(

        @Size(max = 100, message = "Title must be up to 100 characters")
        String title,

        @Size(max = 255, message = "Description must be up to 255 characters")
        String description,

        @FutureOrPresent(message = "Event date must be in the present or future")
        OffsetDateTime eventDate,

        String location,
        UUID categoryId,
        UUID createdBy,
        Set<UUID> tags,
        TagUpdateMode tagUpdateMode
) {
    public enum TagUpdateMode {
        REPLACE,
        ADD,
        REMOVE
    }
}
