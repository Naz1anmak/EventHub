package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserCreateDto(

        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @Email
        String email
) {
}
