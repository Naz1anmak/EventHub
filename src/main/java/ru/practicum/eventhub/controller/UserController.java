package ru.practicum.eventhub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.eventhub.api.UsersApi;
import ru.practicum.eventhub.api.mapper.UserApiMapper;
import ru.practicum.eventhub.api.mapper.UserMetadataApiMapper;
import ru.practicum.eventhub.api.model.*;
import ru.practicum.eventhub.domain.service.UserMetadataService;
import ru.practicum.eventhub.domain.service.UserService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {
    private final UserService userService;
    private final UserMetadataService userMetadataService;
    private final UserApiMapper userApiMapper;
    private final UserMetadataApiMapper userMetadataApiMapper;

    @Override
    public ResponseEntity<UserDto> createUser(UserCreateDto userCreateDto) {
        var serviceCreateDto = userApiMapper.toServiceDto(userCreateDto);
        var serviceResult = userService.createUser(serviceCreateDto);
        var apiResult = userApiMapper.toApiDto(serviceResult);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResult);
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
        var serviceDto = userService.getUserById(userId);
        return ResponseEntity.ok(userApiMapper.toApiDto(serviceDto));
    }

    @Override
    public ResponseEntity<PagedUserMetadataDtoResponse> getUserMetadata(Integer page, Integer size) {
        var servicePage = userMetadataService.getUserMetadata(PageRequest.of(page, size));
        return ResponseEntity.ok(userMetadataApiMapper.toApiPage(servicePage));
    }

    @Override
    public ResponseEntity<UserMetadataDto> getUserMetadataById(UUID userId) {
        var serviceDto = userMetadataService.getByUserId(userId);
        return ResponseEntity.ok(userMetadataApiMapper.toApiDto(serviceDto));
    }

    @Override
    public ResponseEntity<PagedUserDtoResponse> getUsers(Integer page, Integer size) {
        var servicePage = userService.getUsers(PageRequest.of(page, size));
        return ResponseEntity.ok(userApiMapper.toApiPage(servicePage));
    }

    @Override
    public ResponseEntity<UserDto> updateUser(UUID userId, UserUpdateDto userUpdateDto) {
        var serviceUpdateDto = userApiMapper.toServiceDto(userUpdateDto);
        var serviceResult = userService.updateUser(userId, serviceUpdateDto);
        var apiResult = userApiMapper.toApiDto(serviceResult);

        return ResponseEntity.ok(apiResult);
    }
}
