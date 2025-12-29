package ru.practicum.eventhub.domain.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.exception.ConflictException;
import ru.practicum.eventhub.domain.model.Tag;
import ru.practicum.eventhub.domain.repository.TagRepository;

import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagReadService {
    private final TagRepository tagRepository;

    public Tag findById(UUID tagId) {
        return tagRepository.findById(tagId).orElseThrow(() -> {
            log.error("Тег с id={} не найден", tagId);
            return new EntityNotFoundException("Тег с id=" + tagId + " не найден");
        });
    }

    public Tag findByIdAndEventsId(UUID tagId, UUID eventId) {
        return tagRepository.findByIdAndEventsId(tagId, eventId).orElseThrow(() -> {
            log.error("Тег с id={} для события с id={} не найден", tagId, eventId);
            return new EntityNotFoundException("Тег с id=" + tagId + " для события с id=" + eventId + " не найден");
        });
    }

    public void checkExistsByName(String name) {
        if (tagRepository.existsByName(name)) {
            log.error("Тег с именем '{}' уже существует", name);
            throw new ConflictException("Тег с именем '" + name + "' уже существует");
        }
    }

    public boolean existsByIdAndEventsId(UUID tagId, UUID eventId) {
        return tagRepository.existsByIdAndEventsId(tagId, eventId);
    }
}
