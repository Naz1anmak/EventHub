package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.Size;

public record TagUpdateDto(

        @Size(min = 3, max = 50, message = "Иmя должно быть от 3 до 50 символов")
        String name,

        @Size(max = 255, message = "Описание не должно превышать 255 символов")
        String description
) {
}
