package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.exception.NotFoundException;
import ru.practicum.eventhub.domain.model.Tag;
import ru.practicum.eventhub.domain.repository.TagRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagReader {
    private final TagRepository tagRepository;

    public Tag findById(UUID id) {
        return tagRepository.findById(id).orElseThrow(() -> {
            log.error("Тег с id={} не найден", id);
            return new NotFoundException("Тег с id='" + id + "' не найден.");
        });
    }

    public Tag findByIdAndEventsId(UUID tagId, UUID eventId) {
        return tagRepository.findByIdAndEventsId(tagId, eventId).orElseThrow(() -> {
            log.error("Тег с id={} для события с id={} не найден", tagId, eventId);
            return new NotFoundException("Тег с id=" + tagId + " для события с id=" + eventId + " не найден");
        });
    }

    public Set<Tag> getTagsByIds(Set<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return Set.of();
        }

        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(ids));
        if (tags.size() != ids.size()) {
            Set<UUID> foundIds = tags.stream().map(Tag::getId).collect(Collectors.toSet());
            Set<UUID> missing = new HashSet<>(ids);
            missing.removeAll(foundIds);
            log.error("Не найдены теги c id={}", missing);
            throw new NotFoundException("Не найдены теги c id=" + missing);
        }

        return tags;
    }
}
