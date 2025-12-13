package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.Size;

public record TagUpdateDto(

        @Size(max = 50, message = "Иmя должно быть до 50 символов")
        String name,

        @Size(max = 255, message = "Описание не должно превышать 255 символов")
        String description
) {
}
