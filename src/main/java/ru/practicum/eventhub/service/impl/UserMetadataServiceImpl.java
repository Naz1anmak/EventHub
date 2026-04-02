package ru.practicum.eventhub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.response.UserMetadataDto;
import ru.practicum.eventhub.api.mapper.UserMetadataMapper;
import ru.practicum.eventhub.api.model.PageOfMetadata;
import ru.practicum.eventhub.model.UserMetadata;
import ru.practicum.eventhub.repository.UserMetadataRepository;
import ru.practicum.eventhub.service.UserMetadataService;
import ru.practicum.eventhub.util.PageValidator;

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
    public PageOfMetadata getUserMetadata(Pageable pageable) {
        Page<UserMetadata> page = userMetadataRepository.findAll(pageable);
        PageValidator.validatePage(page);

        log.info("Получена страница user-metadata: страница={}, размер={}",
                pageable.getPageNumber(), pageable.getPageSize());
        return toApiPage(page, userMetadataMapper);
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

    private PageOfMetadata toApiPage(Page<UserMetadata> page, UserMetadataMapper mapper) {
        PageOfMetadata apiPage = new PageOfMetadata();
        apiPage.setContent(page.getContent().stream().map(mapper::toDto).toList());
        apiPage.setPageNumber(page.getNumber());
        apiPage.setPageSize(page.getSize());
        apiPage.setTotalElements(page.getTotalElements());
        apiPage.setTotalPages(page.getTotalPages());
        return apiPage;
    }
}
