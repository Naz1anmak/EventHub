package ru.practicum.eventhub.domain.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import ru.practicum.eventhub.api.dto.request.UserCreateDto;
import ru.practicum.eventhub.api.dto.request.UserUpdateDto;
import ru.practicum.eventhub.api.dto.response.UserDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.model.User;

import java.util.UUID;

public interface UserService {
    UserDto createUser(@Valid UserCreateDto dto);

    PagedResponse<UserDto> getUsers(Pageable pageable);

    UserDto getUserById(UUID id);

    UserDto updateUser(UUID id, @Valid UserUpdateDto dto);

    void deleteUser(UUID id);

    User getUserByIdOrThrow(UUID userId);
}
