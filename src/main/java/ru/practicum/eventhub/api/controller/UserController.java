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
import ru.practicum.eventhub.api.dto.request.UserMetadataCreateDto;
import ru.practicum.eventhub.api.dto.request.UserMetadataUpdateDto;
import ru.practicum.eventhub.api.dto.request.UserUpdateDto;
import ru.practicum.eventhub.api.dto.response.UserDto;
import ru.practicum.eventhub.api.dto.response.UserMetadataDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.service.UserMetadataService;
import ru.practicum.eventhub.domain.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/users/v1/api")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMetadataService userMetadataService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserCreateDto dto) {
        return userService.createUser(dto);
    }

    @GetMapping
    public PagedResponse<UserDto> getUsers(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        return userService.getUsers(PageRequest.of(page, size));
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable UUID userId) {
        return userService.getUserById(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable UUID userId, @Valid @RequestBody UserUpdateDto dto) {
        return userService.updateUser(userId, dto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
    }

    @PostMapping("/{userId}/metadata")
    @ResponseStatus(HttpStatus.CREATED)
    public UserMetadataDto createMetadataForUser(@PathVariable UUID userId,
                                                 @Valid @RequestBody UserMetadataCreateDto dto) {
        return userMetadataService.createForUser(userId, dto);
    }

    @GetMapping("/metadata")
    public PagedResponse<UserMetadataDto> getUserMetadata(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        return userMetadataService.getUserMetadata(PageRequest.of(page, size));
    }

    @GetMapping("/{userId}/metadata")
    public UserMetadataDto getMetadataByUserId(@PathVariable UUID userId) {
        return userMetadataService.getByUserId(userId);
    }

    @PatchMapping("/{userId}/metadata")
    public UserMetadataDto updateMetadataForUser(@PathVariable UUID userId,
                                                 @Valid @RequestBody UserMetadataUpdateDto dto) {
        return userMetadataService.updateByUserId(userId, dto);
    }

    @DeleteMapping("/{userId}/metadata")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMetadataForUser(@PathVariable UUID userId) {
        userMetadataService.deleteByUserId(userId);
    }
}
