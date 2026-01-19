package ru.practicum.eventhub.domain.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.exception.ConflictException;
import ru.practicum.eventhub.domain.model.User;
import ru.practicum.eventhub.domain.repository.UserRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserReadService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.error("Пользователь с id={} не найден", id);
            return new EntityNotFoundException("Пользователь с id=" + id + " не найден");
        });
    }

    @Transactional(readOnly = true)
    public void checkExistsByUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            log.error("Пользователь с именем '{}' уже существует", username);
            throw new ConflictException("Пользователь с именем '" + username + "' уже существует");
        }
    }

    @Transactional(readOnly = true)
    public void checkExistsByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            log.error("Пользователь с email '{}' уже существует", email);
            throw new ConflictException("Пользователь с email '" + email + "' уже существует");
        }
    }
}
