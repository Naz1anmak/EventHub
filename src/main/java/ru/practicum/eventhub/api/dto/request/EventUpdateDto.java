package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.Set;

public record EventUpdateDto(

        @Size(min = 3, max = 100, message = "Заголовок должен быть от 3 до 100 символов")
        String title,

        @Size(max = 255, message = "Описание не должно превышать 255 символов")
        String description,

        @FutureOrPresent(message = "Дата и время события должны быть в будущем или настоящем")
        OffsetDateTime eventDate,

        String location,

        @NotNull(message = "Список тегов не должен быть null")
        Set<TagCreateDto> tags
) {
}
