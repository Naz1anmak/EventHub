package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.exception.NotFoundException;
import ru.practicum.eventhub.domain.model.UserMetadata;
import ru.practicum.eventhub.domain.repository.UserMetadataRepository;

import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserMetadataReader {
    private final UserMetadataRepository userMetadataRepository;

    public UserMetadata findByUserId(UUID userId) {
        return userMetadataRepository.findByUserId(userId).orElseThrow(() -> {
            log.warn("User-metadata для user с id={} не найдена.", userId);
            return new NotFoundException("User-metadata для user с id=" + userId + " не найдена.");
        });
    }
}
