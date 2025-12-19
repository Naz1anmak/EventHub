package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ProjectCreateDto(

        @NotBlank(message = "Иmя не должно быть пустым")
        @Size(min = 3, max = 100, message = "Иmя должно быть от 3 до 100 символов")
        String name,

        @Size(max = 255, message = "Описание должно быть до 255 символов")
        String description,

        @NotNull(message = "Поле ownerId не должно быть null")
        UUID ownerId
) {
}
