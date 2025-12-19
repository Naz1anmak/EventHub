package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateDto(

        @NotBlank(message = "Username не должен быть пустым")
        @Size(min = 3, max = 50, message = "Username должен быть от 3 до 50 символов")
        String username,

        @Email(message = "Email должен быть корректным")
        String email,

        @NotNull(message = "Metadata не должна быть null")
        UserMetadataCreateDto metadata
) {
}
