package ru.practicum.eventhub.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.request.UserCreateDto;
import ru.practicum.eventhub.api.dto.request.UserUpdateDto;
import ru.practicum.eventhub.api.exception.ConflictException;
import ru.practicum.eventhub.model.User;
import ru.practicum.eventhub.service.impl.MetadataReadService;
import ru.practicum.eventhub.service.impl.UserReadService;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserValidationService {
    private final UserReadService userReadService;
    private final MetadataReadService metadataReadService;

    @Transactional(propagation = REQUIRES_NEW, readOnly = true)
    public void validateCreate(UserCreateDto dto) {
        userReadService.checkExistsByUsername(dto.username());
        userReadService.checkExistsByEmail(dto.email());
        metadataReadService.checkExistsByPhone(dto.metadata().phone());
    }

    @Transactional(propagation = REQUIRES_NEW, readOnly = true)
    public void validateUpdate(UserUpdateDto dto, User user) {
        validateUsername(dto.username(), user.getUsername());
        validateEmail(dto.email(), user.getEmail());
        validatePhone(dto.metadata() != null ? dto.metadata().phone() : null, user.getMetadata().getPhone());
    }

    @Transactional(readOnly = true)
    public void validateUsername(String newUsername, String currentUsername) {
        if (newUsername == null) return;
        if (newUsername.equals(currentUsername)) {
            log.error("Новое имя пользователя совпадает с текущим");
            throw new ConflictException("Новое имя пользователя совпадает с текущим");
        }
        userReadService.checkExistsByUsername(newUsername);
    }

    @Transactional(readOnly = true)
    public void validateEmail(String newEmail, String currentEmail) {
        if (newEmail == null) return;
        if (newEmail.equals(currentEmail)) {
            log.error("Новый email совпадает с текущим");
            throw new ConflictException("Новый email совпадает с текущим");
        }
        userReadService.checkExistsByEmail(newEmail);
    }

    @Transactional(readOnly = true)
    public void validatePhone(String newPhone, String currentPhone) {
        if (newPhone == null) return;
        if (newPhone.equals(currentPhone)) {
            log.error("Новый номер телефона совпадает с текущим");
            throw new ConflictException("Новый номер телефона совпадает с текущим");
        }
        metadataReadService.checkExistsByPhone(newPhone);
    }
}
