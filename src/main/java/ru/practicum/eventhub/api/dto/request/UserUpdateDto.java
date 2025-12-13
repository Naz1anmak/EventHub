package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(

        @Size(max = 50, message = "Username должен быть до 50 символов")
        String username,

        @Email(message = "Email должен быть корректным")
        String email
) {
}
