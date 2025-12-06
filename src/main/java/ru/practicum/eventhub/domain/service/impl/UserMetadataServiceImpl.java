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
import ru.practicum.eventhub.domain.exception.NotFoundException;
import ru.practicum.eventhub.domain.model.User;
import ru.practicum.eventhub.domain.model.UserMetadata;
import ru.practicum.eventhub.domain.repository.UserMetadataRepository;
import ru.practicum.eventhub.domain.service.UserMetadataService;
import ru.practicum.eventhub.domain.service.UserService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMetadataServiceImpl implements UserMetadataService {
    private final UserMetadataRepository userMetadataRepository;
    private final UserMetadataMapper userMetadataMapper;
    private final UserService userService;

    @Override
    @Transactional
    public UserMetadataDto createUserMetadata(UserMetadataCreateDto dto) {
        UUID userId = dto.userId();
        User user = userService.getUserByIdOrThrow(userId);

        UserMetadata userMetadata = userMetadataMapper.fromCreateDto(dto, user);
        userMetadata = userMetadataRepository.save(userMetadata);

        log.info("Создана user-metadata: {}", userMetadata);
        return userMetadataMapper.toDto(userMetadata);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UserMetadataDto> getUserMetadata(Pageable pageable) {
        Page<UserMetadata> userMetadataPage = userMetadataRepository.findAll(pageable);

        log.info("Получена страница user-metadata: номер={}, размер={}",
                userMetadataPage.getNumber(), userMetadataPage.getSize());
        return new PagedResponse<>(
                userMetadataPage.getContent().stream().map(userMetadataMapper::toDto).toList(),
                userMetadataPage.getNumber(),
                userMetadataPage.getSize(),
                userMetadataPage.getTotalElements(),
                userMetadataPage.getTotalPages()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserMetadataDto getUserMetadataById(UUID id) {
        log.info("Запрошена user-metadata с id={}", id);
        return userMetadataMapper.toDto(getUserMetadataByIdOrThrow(id));
    }

    @Override
    @Transactional
    public UserMetadataDto updateUserMetadata(UUID id, UserMetadataUpdateDto dto) {
        UserMetadata userMetadata = getUserMetadataByIdOrThrow(id);

        userMetadataMapper.updateFromDto(dto, userMetadata);
        userMetadata = userMetadataRepository.save(userMetadata);

        log.info("Обновлена user-metadata c id={}", id);
        return userMetadataMapper.toDto(userMetadata);
    }

    @Override
    @Transactional
    public void deleteUserMetadata(UUID id) {
        getUserMetadataByIdOrThrow(id);
        userMetadataRepository.deleteById(id);
        log.info("Удалена user-metadata с id={}", id);
    }

    private UserMetadata getUserMetadataByIdOrThrow(UUID id) {
        return userMetadataRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User-metadata c id=" + id + " не найдена"));
    }
}
