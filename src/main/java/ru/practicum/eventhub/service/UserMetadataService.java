package ru.practicum.eventhub.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.eventhub.api.dto.response.UserMetadataDto;
import ru.practicum.eventhub.api.model.PageOfMetadata;

import java.util.UUID;

public interface UserMetadataService {
    PageOfMetadata getUserMetadata(Pageable pageable);

    UserMetadataDto getByUserId(UUID userId);
}
