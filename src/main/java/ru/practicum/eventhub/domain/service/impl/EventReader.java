package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.exception.NotFoundException;
import ru.practicum.eventhub.domain.model.Event;
import ru.practicum.eventhub.domain.repository.EventRepository;

import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventReader {
    private final EventRepository eventRepository;

    public Event findById(UUID id) {
        return eventRepository.findById(id).orElseThrow(() -> {
            log.warn("Событие с id={} не найдено", id);
            return new NotFoundException("Событие с id=" + id + " не найдено");
        });
    }
}
