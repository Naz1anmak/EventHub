package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.response.UserMetadataDto;
import ru.practicum.eventhub.api.mapper.UserMetadataMapper;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.model.UserMetadata;
import ru.practicum.eventhub.domain.repository.UserMetadataRepository;
import ru.practicum.eventhub.domain.service.UserMetadataService;
import ru.practicum.eventhub.domain.util.PageValidator;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMetadataServiceImpl implements UserMetadataService {
    private final UserMetadataRepository userMetadataRepository;
    private final MetadataReadService metadataReadService;
    private final UserMetadataMapper userMetadataMapper;
    private final UserReadService userReadService;

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UserMetadataDto> getUserMetadata(Pageable pageable) {
        Page<UserMetadata> page = userMetadataRepository.findAll(pageable);
        PageValidator.validatePage(page);

        log.info("Получена страница user-metadata: страница={}, размер={}",
                pageable.getPageNumber(), pageable.getPageSize());
        return PagedResponse.from(page, userMetadataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "userMetadata", key = "#userId")
    public UserMetadataDto getByUserId(UUID userId) {
        userReadService.findById(userId);
        UserMetadata userMetadata = metadataReadService.findByUserId(userId);

        UUID metadataId = userMetadata.getId();
        log.info("Запрошена user-metadata с id={}, userId={}", metadataId, userId);
        return userMetadataMapper.toDto(userMetadata);
    }
}
