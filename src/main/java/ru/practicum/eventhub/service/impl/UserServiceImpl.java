package ru.practicum.eventhub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.request.UserCreateDto;
import ru.practicum.eventhub.api.dto.request.UserUpdateDto;
import ru.practicum.eventhub.api.dto.response.UserDto;
import ru.practicum.eventhub.api.mapper.UserMapper;
import ru.practicum.eventhub.api.model.PageOfUsers;
import ru.practicum.eventhub.model.User;
import ru.practicum.eventhub.repository.UserRepository;
import ru.practicum.eventhub.service.UserService;
import ru.practicum.eventhub.util.PageValidator;
import ru.practicum.eventhub.validation.UserValidationService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserReadService userReadService;
    private final UserValidationService userValidationService;

    @Override
    @Transactional
    public UserDto createUser(UserCreateDto dto) {
        userValidationService.validateCreate(dto);

        User user = userMapper.fromCreateDto(dto);
        user.getMetadata().setUser(user);

        user = userRepository.save(user);

        log.info("Создан новый пользователь с id={}", user.getId());
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PageOfUsers getUsers(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        PageValidator.validatePage(page);

        log.info("Получена страница пользователей: страница={}, размер={}",
                pageable.getPageNumber(), pageable.getPageSize());
        return toApiPage(page, userMapper);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#id")
    public UserDto getUserById(UUID id) {
        User user = userReadService.findById(id);
        log.info("Запрошен пользователь с id={}", id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    @CachePut(value = "users", key = "#id")
    public UserDto updateUser(UUID id, UserUpdateDto dto) {
        User user = userReadService.findByIdForUpdate(id);

        userValidationService.validateUpdate(dto, user);

        user = userMapper.updateUserFromDto(dto, user);

        user = userRepository.save(user);
        log.info("Обновлен пользователь с id={}", id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "users", key = "#id"),
            @CacheEvict(value = "userMetadata", key = "#id")
    })
    public void deleteUser(UUID id) {
        userReadService.findById(id);
        userRepository.deleteById(id);
        log.info("Удален пользователь с id={}", id);
    }

    private PageOfUsers toApiPage(Page<User> page, UserMapper mapper) {
        PageOfUsers apiPage = new PageOfUsers();
        apiPage.setContent(page.getContent().stream().map(mapper::toDto).toList());
        apiPage.setPageNumber(page.getNumber());
        apiPage.setPageSize(page.getSize());
        apiPage.setTotalElements(page.getTotalElements());
        apiPage.setTotalPages(page.getTotalPages());
        return apiPage;
    }
}
