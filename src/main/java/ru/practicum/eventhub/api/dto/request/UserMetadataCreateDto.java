package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserMetadataCreateDto(

        @NotBlank(message = "Имя не должно быть пустым")
        @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 30 символов")
        String firstName,

        @NotBlank(message = "Фамилия не должна быть пустой")
        @Size(min = 2, max = 30, message = "Фамилия должна быть от 2 до 30 символов")
        String lastName,

        String address,

        @Size(max = 16, message = "Номер телефона должен быть до 16 символов")
        String phone
) {
}
