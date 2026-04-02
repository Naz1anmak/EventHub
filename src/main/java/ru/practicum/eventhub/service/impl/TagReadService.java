package ru.practicum.eventhub.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.response.TagDto;
import ru.practicum.eventhub.api.exception.ConflictException;
import ru.practicum.eventhub.api.mapper.TagMapper;
import ru.practicum.eventhub.model.Tag;
import ru.practicum.eventhub.repository.TagRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagReadService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Transactional(readOnly = true)
    public Tag findById(UUID tagId) {
        return tagRepository.findById(tagId).orElseThrow(() -> {
            log.error("Тег с id={} не найден", tagId);
            return new EntityNotFoundException("Тег с id=" + tagId + " не найден");
        });
    }

    @CachePut(value = "tags", key = "#tagId")
    @Transactional(readOnly = true)
    public TagDto findDtoById(UUID tagId) {
        Tag tag = findById(tagId);
        log.info("Обновлен кеш тега с id={}", tagId);
        return tagMapper.toDto(tag);
    }

    @Transactional(readOnly = true)
    public Tag findByIdAndEventsId(UUID tagId, UUID eventId) {
        return tagRepository.findByIdAndEventsId(tagId, eventId).orElseThrow(() -> {
            log.error("Тег с id={} для события с id={} не найден", tagId, eventId);
            return new EntityNotFoundException("Тег с id=" + tagId + " для события с id=" + eventId + " не найден");
        });
    }

    @Transactional(readOnly = true)
    public List<Tag> findAllByIdInAndEventsId(Set<UUID> tagIds, UUID eventId) {
        return tagRepository.findAllByIdInAndEventsId(tagIds, eventId);
    }

    @Transactional(readOnly = true)
    public void checkExistsByName(String name) {
        if (tagRepository.existsByName(name)) {
            log.error("Тег с именем '{}' уже существует", name);
            throw new ConflictException("Тег с именем '" + name + "' уже существует");
        }
    }

    @Transactional(readOnly = true)
    public boolean existsByIdAndEventsId(UUID tagId, UUID eventId) {
        return tagRepository.existsByIdAndEventsId(tagId, eventId);
    }
}
