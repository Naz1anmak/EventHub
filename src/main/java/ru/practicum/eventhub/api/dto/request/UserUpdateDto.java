package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(

        @Size(max = 50, message = "Username must be up to 50 characters")
        String username,

        @Email
        String email
) {
}
