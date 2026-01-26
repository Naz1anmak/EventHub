package ru.practicum.eventhub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.eventhub.api.UsersApi;
import ru.practicum.eventhub.api.dto.request.UserCreateDto;
import ru.practicum.eventhub.api.dto.request.UserUpdateDto;
import ru.practicum.eventhub.api.dto.response.UserDto;
import ru.practicum.eventhub.api.dto.response.UserMetadataDto;
import ru.practicum.eventhub.api.model.PageOfMetadata;
import ru.practicum.eventhub.api.model.PageOfUsers;
import ru.practicum.eventhub.application.MetadataApplicationService;
import ru.practicum.eventhub.application.UserApplicationService;
import ru.practicum.eventhub.domain.service.UserMetadataService;
import ru.practicum.eventhub.domain.service.UserService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {
    private final UserApplicationService userApplicationService;
    private final MetadataApplicationService metadataApplicationService;
    private final UserService userService;
    private final UserMetadataService userMetadataService;

    @Override
    public ResponseEntity<UserDto> createUser(UserCreateDto userCreateDto) {
        UserDto userDto = userService.createUser(userCreateDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userDto);
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Override
    public ResponseEntity<UserDto> getUserById(UUID userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    @Override
    public ResponseEntity<PageOfMetadata> getUserMetadata(Integer page, Integer size) {
        PageOfMetadata pageOfUsers = metadataApplicationService.getUserMetadata(PageRequest.of(page, size));
        return ResponseEntity.ok(pageOfUsers);
    }

    @Override
    public ResponseEntity<UserMetadataDto> getUserMetadataById(UUID userId) {
        UserMetadataDto metadataDto = userMetadataService.getByUserId(userId);
        return ResponseEntity.ok(metadataDto);
    }

    @Override
    public ResponseEntity<PageOfUsers> getUsers(Integer page, Integer size) {
        PageOfUsers pageOfUsers = userApplicationService.getUsers(PageRequest.of(page, size));
        return ResponseEntity.ok(pageOfUsers);
    }

    @Override
    public ResponseEntity<UserDto> updateUser(UUID userId, UserUpdateDto userUpdateDto) {
        UserDto userDto = userService.updateUser(userId, userUpdateDto);
        return ResponseEntity.ok(userDto);
    }
}
