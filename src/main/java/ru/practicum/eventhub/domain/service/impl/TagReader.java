package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.exception.NotFoundException;
import ru.practicum.eventhub.domain.model.Tag;
import ru.practicum.eventhub.domain.repository.TagRepository;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagReader {
    private final TagRepository tagRepository;

    public Tag findByIdAndEventsId(UUID tagId, UUID eventId) {
        return tagRepository.findByIdAndEventsId(tagId, eventId).orElseThrow(() -> {
            log.error("Тег с id={} для события с id={} не найден", tagId, eventId);
            return new NotFoundException("Тег с id=" + tagId + " для события с id=" + eventId + " не найден");
        });
    }

    public Map<String, Tag> findByNames(Set<String> names) {
        if (names == null || names.isEmpty()) {
            log.info("Набор имен тегов пустой, возвращается пустая мапа");
            return Map.of();
        }

        Set<Tag> tags = new HashSet<>(tagRepository.findAllByNameIn(names));
        return tags.stream().collect(Collectors.toMap(Tag::getName, tag -> tag));
    }
}
