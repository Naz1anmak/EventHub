package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.practicum.eventhub.api.validation.UniqueCategoryName;

public record CategoryCreateDto(

        @NotBlank(message = "Name must not be blank")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        @UniqueCategoryName
        String name,

        @Size(max = 255, message = "Description must be up to 255 characters")
        String description
) {
}
