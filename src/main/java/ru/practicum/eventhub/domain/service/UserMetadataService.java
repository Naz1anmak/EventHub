package ru.practicum.eventhub.domain.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import ru.practicum.eventhub.api.dto.request.UserMetadataCreateDto;
import ru.practicum.eventhub.api.dto.request.UserMetadataUpdateDto;
import ru.practicum.eventhub.api.dto.response.UserMetadataDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;

import java.util.UUID;

public interface UserMetadataService {
    UserMetadataDto createUserMetadata(@Valid UserMetadataCreateDto dto);

    PagedResponse<UserMetadataDto> getUserMetadata(Pageable pageable);

    UserMetadataDto getUserMetadataById(UUID id);

    UserMetadataDto updateUserMetadata(UUID id, @Valid UserMetadataUpdateDto dto);

    void deleteUserMetadata(UUID id);
}
