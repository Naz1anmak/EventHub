package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.Size;

public record CategoryUpdateDto(

        @Size(max = 100, message = "Иmя должно быть до 100 символов")
        String name,

        @Size(max = 255, message = "Описание должно быть до 255 символов")
        String description
) {
}
