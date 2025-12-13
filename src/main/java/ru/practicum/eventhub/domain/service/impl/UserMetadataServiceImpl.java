package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.request.UserMetadataCreateDto;
import ru.practicum.eventhub.api.dto.request.UserMetadataUpdateDto;
import ru.practicum.eventhub.api.dto.response.UserMetadataDto;
import ru.practicum.eventhub.api.mapper.UserMetadataMapper;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.model.User;
import ru.practicum.eventhub.domain.model.UserMetadata;
import ru.practicum.eventhub.domain.repository.UserMetadataRepository;
import ru.practicum.eventhub.domain.service.UserMetadataService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMetadataServiceImpl implements UserMetadataService {
    private final UserMetadataRepository userMetadataRepository;
    private final UserMetadataReader userMetadataReader;
    private final UserMetadataMapper userMetadataMapper;
    private final UserReader userReader;

    @Override
    @Transactional
    public UserMetadataDto createForUser(UUID userId, UserMetadataCreateDto dto) {
        User user = userReader.findById(userId);

        UserMetadata userMetadata = userMetadataMapper.fromCreateDto(dto, user);
        userMetadata = userMetadataRepository.save(userMetadata);

        log.info("Создана user-metadata с id={} для user c id={}", userMetadata.getId(), userId);
        return userMetadataMapper.toDto(userMetadata);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UserMetadataDto> getUserMetadata(Pageable pageable) {
        Page<UserMetadata> userMetadataPage = userMetadataRepository.findAll(pageable);

        log.info("Получена страница user-metadata: страница={}, размер={}",
                pageable.getPageNumber(), pageable.getPageSize());
        return PagedResponse.from(userMetadataPage, userMetadataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserMetadataDto getByUserId(UUID userId) {
        UserMetadata userMetadata = userMetadataReader.findByUserId(userId);

        UUID metadataId = userMetadata.getId();
        log.info("Запрошена user-metadata с id={}, userId={}", metadataId, userId);
        return userMetadataMapper.toDto(userMetadata);
    }

    @Override
    @Transactional
    public UserMetadataDto updateByUserId(UUID userId, UserMetadataUpdateDto dto) {
        UserMetadata userMetadata = userMetadataReader.findByUserId(userId);

        userMetadataMapper.updateFromDto(dto, userMetadata);
        userMetadata = userMetadataRepository.save(userMetadata);

        log.info("Обновлена user-metadata c id={}, userId={}", userMetadata.getId(), userId);
        return userMetadataMapper.toDto(userMetadata);
    }

    @Override
    @Transactional
    public void deleteByUserId(UUID userId) {
        UserMetadata userMetadata = userMetadataReader.findByUserId(userId);
        UUID metadataId = userMetadata.getId();
        userMetadataRepository.deleteById(metadataId);
        log.info("Удалена user-metadata c id={}, userId={}", metadataId, userId);
    }
}
