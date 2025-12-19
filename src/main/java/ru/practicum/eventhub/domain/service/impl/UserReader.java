package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.exception.NotFoundException;
import ru.practicum.eventhub.domain.model.User;
import ru.practicum.eventhub.domain.repository.UserRepository;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserReader {
    private final UserRepository userRepository;

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.warn("Пользователь с id={} не найден.", id);
            return new NotFoundException("Пользователь с id=" + id + " не найден.");
        });
    }

    public Map<UUID, User> getUsersByIds(Set<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }

        Set<User> users = new HashSet<>(userRepository.findAllById(ids));
        if (users.size() != ids.size()) {
            Set<UUID> foundIds = users.stream().map(User::getId).collect(Collectors.toSet());
            Set<UUID> missing = new HashSet<>(ids);
            missing.removeAll(foundIds);
            log.warn("Не найдены пользователи c id={}", missing);
            throw new NotFoundException("Не найдены пользователи c id=" + missing);
        }

        return users.stream().collect(Collectors.toMap(User::getId, user -> user));
    }
}
