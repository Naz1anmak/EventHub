package ru.practicum.eventhub.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import ru.practicum.eventhub.api.dto.request.UserCreateDto;
import ru.practicum.eventhub.api.dto.request.UserUpdateDto;
import ru.practicum.eventhub.api.dto.response.UserDto;
import ru.practicum.eventhub.api.model.PageOfUsers;

import java.util.UUID;

public interface UserService {
    UserDto createUser(@Valid UserCreateDto dto);

    PageOfUsers getUsers(Pageable pageable);

    UserDto getUserById(UUID id);

    UserDto updateUser(UUID id, @Valid UserUpdateDto dto);

    void deleteUser(UUID id);
}
