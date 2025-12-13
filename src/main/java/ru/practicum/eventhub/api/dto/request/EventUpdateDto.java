package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import ru.practicum.eventhub.domain.model.TagUpdateMode;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

public record EventUpdateDto(

        @Size(max = 100, message = "Заголовок должен быть до 100 символов")
        String title,

        @Size(max = 255, message = "Описание не должно превышать 255 символов")
        String description,

        @FutureOrPresent(message = "Дата и время события должны быть в будущем или настоящем")
        OffsetDateTime eventDate,

        String location,
        UUID categoryId,
        UUID createdBy,
        Set<UUID> tags,
        TagUpdateMode tagUpdateMode
) {
}
