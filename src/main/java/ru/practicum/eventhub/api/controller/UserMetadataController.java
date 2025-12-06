package ru.practicum.eventhub.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventhub.api.dto.request.UserMetadataCreateDto;
import ru.practicum.eventhub.api.dto.request.UserMetadataUpdateDto;
import ru.practicum.eventhub.api.dto.response.UserMetadataDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.service.UserMetadataService;

import java.util.UUID;

@RestController
@RequestMapping("/user-metadata")
@Validated
@RequiredArgsConstructor
public class UserMetadataController {
    private final UserMetadataService userMetadataService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserMetadataDto createUserMetadata(@Valid @RequestBody UserMetadataCreateDto dto) {
        return userMetadataService.createUserMetadata(dto);
    }

    @GetMapping
    public PagedResponse<UserMetadataDto> getUserMetadata(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        return userMetadataService.getUserMetadata(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public UserMetadataDto getUserMetadataById(@PathVariable UUID id) {
        return userMetadataService.getUserMetadataById(id);
    }

    @PatchMapping("/{id}")
    public UserMetadataDto updateUserMetadata(@PathVariable UUID id, @Valid @RequestBody UserMetadataUpdateDto dto) {
        return userMetadataService.updateUserMetadata(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserMetadata(@PathVariable UUID id) {
        userMetadataService.deleteUserMetadata(id);
    }
}
