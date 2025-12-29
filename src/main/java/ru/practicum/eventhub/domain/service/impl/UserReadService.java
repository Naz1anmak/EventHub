package ru.practicum.eventhub.domain.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.exception.ConflictException;
import ru.practicum.eventhub.domain.model.User;
import ru.practicum.eventhub.domain.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserReadService {
    private final UserRepository userRepository;

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.error("Пользователь с id={} не найден", id);
            return new EntityNotFoundException("Пользователь с id=" + id + " не найден");
        });
    }

    public Map<UUID, User> getUsersByIds(Set<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            log.info("Список id пользователей пустой, возвращается пустая мапа");
            return Map.of();
        }

        List<User> users = new ArrayList<>(userRepository.findAllById(ids));
        if (users.size() != ids.size()) {
            List<UUID> foundIds = users.stream().map(User::getId).toList();
            List<UUID> missing = new ArrayList<>(ids);
            missing.removeAll(foundIds);
            log.error("Не найдены пользователи c id={}", missing);
            throw new EntityNotFoundException("Не найдены пользователи c id=" + missing);
        }

        return users.stream().collect(Collectors.toMap(User::getId, user -> user));
    }

    public void checkExistsByUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            log.error("Пользователь с именем '{}' уже существует", username);
            throw new ConflictException("Пользователь с именем '" + username + "' уже существует");
        }
    }

    public void checkExistsByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            log.error("Пользователь с email '{}' уже существует", email);
            throw new ConflictException("Пользователь с email '" + email + "' уже существует");
        }
    }
}
