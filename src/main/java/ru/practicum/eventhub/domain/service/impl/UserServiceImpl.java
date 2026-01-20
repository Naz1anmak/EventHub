package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.request.UserCreateDto;
import ru.practicum.eventhub.api.dto.request.UserUpdateDto;
import ru.practicum.eventhub.api.dto.response.UserDto;
import ru.practicum.eventhub.api.exception.ConflictException;
import ru.practicum.eventhub.api.mapper.UserMapper;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.model.User;
import ru.practicum.eventhub.domain.repository.UserRepository;
import ru.practicum.eventhub.domain.service.UserService;
import ru.practicum.eventhub.domain.util.PageValidator;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserReadService userReadService;
    private final MetadataReadService metadataReadService;

    @Override
    @Transactional
    public UserDto createUser(UserCreateDto dto) {
        userReadService.checkExistsByUsername(dto.username());
        userReadService.checkExistsByEmail(dto.email());
        metadataReadService.checkExistsByPhone(dto.metadata().phone());

        User user = userMapper.fromCreateDto(dto);
        user.getMetadata().setUser(user);

        user = userRepository.save(user);

        log.info("Создан новый пользователь с id={}", user.getId());
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UserDto> getUsers(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        PageValidator.validatePage(page);

        log.info("Получена страница пользователей: страница={}, размер={}",
                pageable.getPageNumber(), pageable.getPageSize());
        return PagedResponse.from(page, userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(UUID id) {
        log.info("Запрошен пользователь с id={}", id);
        return userMapper.toDto(userReadService.findById(id));
    }

    @Override
    @Transactional
    public UserDto updateUser(UUID id, UserUpdateDto dto) {
        User user = userReadService.findById(id);

        validateUsernameUpdate(dto.username(), user.getUsername());
        validateEmailUpdate(dto.email(), user.getEmail());
        validatePhoneUpdate(dto.metadata() != null ? dto.metadata().phone() : null, user.getMetadata().getPhone());

        user = userMapper.updateUserFromDto(dto, user);

        user = userRepository.save(user);
        log.info("Обновлен пользователь с id={}", id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        userReadService.findById(id);
        userRepository.deleteById(id);
        log.info("Удален пользователь с id={}", id);
    }

    private void validateUsernameUpdate(String newUsername, String currentUsername) {
        if (newUsername == null) return;
        if (newUsername.equals(currentUsername)) {
            log.error("Новое имя пользователя совпадает с текущим");
            throw new ConflictException("Новое имя пользователя совпадает с текущим");
        }
        userReadService.checkExistsByUsername(newUsername);
    }

    private void validateEmailUpdate(String newEmail, String currentEmail) {
        if (newEmail == null) return;
        if (newEmail.equals(currentEmail)) {
            log.error("Новый email совпадает с текущим");
            throw new ConflictException("Новый email совпадает с текущим");
        }
        userReadService.checkExistsByEmail(newEmail);
    }

    private void validatePhoneUpdate(String newPhone, String currentPhone) {
        if (newPhone == null) return;
        if (newPhone.equals(currentPhone)) {
            log.error("Новый номер телефона совпадает с текущим");
            throw new ConflictException("Новый номер телефона совпадает с текущим");
        }
        metadataReadService.checkExistsByPhone(newPhone);
    }
}
