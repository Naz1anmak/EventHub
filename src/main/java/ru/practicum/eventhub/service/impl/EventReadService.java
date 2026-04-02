package ru.practicum.eventhub.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.response.EventDto;
import ru.practicum.eventhub.api.mapper.EventMapper;
import ru.practicum.eventhub.model.Event;
import ru.practicum.eventhub.repository.EventRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventReadService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Transactional(readOnly = true)
    public Event findById(UUID id) {
        return eventRepository.findById(id).orElseThrow(() -> {
            log.error("Событие с id={} не найдено", id);
            return new EntityNotFoundException("Событие с id=" + id + " не найдено");
        });
    }

    @CachePut(value = "events", key = "#id")
    @Transactional(readOnly = true)
    public EventDto findDtoById(UUID id) {
        Event event = findById(id);
        log.info("Обновлен кеш события с id={}", id);
        return eventMapper.toDto(event);
    }

    @Transactional(readOnly = true)
    public Event findByIdForUpdate(UUID id) {
        return eventRepository.findByIdForUpdate(id).orElseThrow(() -> {
            log.error("Событие с id={} не найдено", id);
            return new EntityNotFoundException("Событие с id=" + id + " не найдено");
        });
    }
}
