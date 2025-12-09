package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.Size;
import ru.practicum.eventhub.api.validation.UniqueCategoryName;

public record CategoryUpdateDto(

        @Size(max = 100, message = "Name must be up to 100 characters")
        @UniqueCategoryName
        String name,

        @Size(max = 255, message = "Description must be up to 255 characters")
        String description
) {
}
