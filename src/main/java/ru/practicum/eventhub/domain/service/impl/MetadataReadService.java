package ru.practicum.eventhub.domain.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.exception.ConflictException;
import ru.practicum.eventhub.domain.model.UserMetadata;
import ru.practicum.eventhub.domain.repository.UserMetadataRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetadataReadService {
    private final UserMetadataRepository userMetadataRepository;

    @Transactional(readOnly = true)
    public UserMetadata findByUserId(UUID userId) {
        return userMetadataRepository.findByUserId(userId).orElseThrow(() -> {
            log.error("User-metadata для user с id={} не найдена", userId);
            return new EntityNotFoundException("User-metadata для user с id=" + userId + " не найдена");
        });
    }

    @Transactional(readOnly = true)
    public void checkExistsByPhone(String phone) {
        if (userMetadataRepository.existsByPhone(phone)) {
            log.error("User-metadata с номером телефона='{}' уже существует", phone);
            throw new ConflictException("User-metadata с номером телефона='" + phone + "' уже существует");
        }
    }
}
