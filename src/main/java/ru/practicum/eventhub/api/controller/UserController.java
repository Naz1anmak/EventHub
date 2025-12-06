package ru.practicum.eventhub.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventhub.api.dto.request.UserCreateDto;
import ru.practicum.eventhub.api.dto.request.UserUpdateDto;
import ru.practicum.eventhub.api.dto.response.UserDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserCreateDto dto) {
        return userService.createUser(dto);
    }

    @GetMapping
    public PagedResponse<UserDto> getUsers(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        return userService.getUsers(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateDto dto) {
        return userService.updateUser(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }
}
