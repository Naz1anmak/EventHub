package ru.practicum.eventhub.domain.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.domain.model.Event;
import ru.practicum.eventhub.domain.repository.EventRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventReadService {
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public Event findById(UUID id) {
        return eventRepository.findById(id).orElseThrow(() -> {
            log.error("Событие с id={} не найдено", id);
            return new EntityNotFoundException("Событие с id=" + id + " не найдено");
        });
    }

    @Transactional(readOnly = true)
    public Event findByIdForUpdate(UUID id) {
        return eventRepository.findByIdForUpdate(id).orElseThrow(() -> {
            log.error("Событие с id={} не найдено", id);
            return new EntityNotFoundException("Событие с id=" + id + " не найдено");
        });
    }
}
