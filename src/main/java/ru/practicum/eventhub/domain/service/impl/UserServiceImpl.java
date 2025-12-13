package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserReader userReader;

    @Override
    @Transactional
    public UserDto createUser(UserCreateDto dto) {
        User user = userMapper.fromCreateDto(dto);
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            log.error(exception.getMessage(), exception);
            throw new ConflictException("Пользователь с username '" + dto.username() + "' уже существует");
        }

        log.info("Создан новый пользователь с id={}", user.getId());
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UserDto> getUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);

        log.info("Получена страница пользователей: страница={}, размер={}",
                pageable.getPageNumber(), pageable.getPageSize());
        return PagedResponse.from(usersPage, userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(UUID id) {
        log.info("Запрошен пользователь с id={}", id);
        return userMapper.toDto(userReader.findById(id));
    }

    @Override
    @Transactional
    public UserDto updateUser(UUID id, UserUpdateDto dto) {
        User user = userReader.findById(id);
        userMapper.updateUserFromDto(dto, user);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            log.error(exception.getMessage(), exception);
            throw new ConflictException("Пользователь с username '" + dto.username() + "' уже существует");
        }

        log.info("Обновлен пользователь с id={}", id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        userReader.findById(id);
        userRepository.deleteById(id);
        log.info("Удален пользователь с id={}", id);
    }
}
