package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.request.EventCreateDto;
import ru.practicum.eventhub.api.dto.request.EventUpdateDto;
import ru.practicum.eventhub.api.dto.response.EventDto;
import ru.practicum.eventhub.api.mapper.EventMapper;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.model.Event;
import ru.practicum.eventhub.domain.repository.EventRepository;
import ru.practicum.eventhub.domain.service.EventService;
import ru.practicum.eventhub.domain.util.PageValidator;
import ru.practicum.eventhub.domain.validation.EventValidationService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventReadService eventReadService;
    private final EventMapper eventMapper;
    private final EventValidationService eventValidationService;

    @Override
    @Transactional
    public EventDto createEvent(EventCreateDto dto) {
        eventValidationService.validateCreate(dto);

        Event event = eventMapper.fromCreateDto(dto);

        event = eventRepository.save(event);
        log.info("Создано событие с id={}", event.getId());
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<EventDto> getEvents(Pageable pageable) {
        Page<Event> page = eventRepository.findAll(pageable);
        PageValidator.validatePage(page);

        log.info("Запрошены события: страница {}, размер {}", pageable.getPageNumber(), pageable.getPageSize());
        return PagedResponse.from(page, eventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto getEventById(UUID id) {
        Event event = eventReadService.findById(id);
        log.info("Запрошено событие с id={}", id);
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional
    @CachePut(value = "events", key = "#id")
    public EventDto updateEvent(UUID id, EventUpdateDto dto) {
        Event event = eventReadService.findByIdForUpdate(id);

        eventValidationService.validateUpdate(dto);

        event = eventMapper.updateEventFromDto(dto, event);

        event = eventRepository.save(event);
        log.info("Обновлено событие с id={}", id);
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional
    @CacheEvict(value = "events", key = "#id")
    public void deleteEvent(UUID id) {
        eventReadService.findById(id);
        eventRepository.deleteById(id);
        log.info("Удалено событие с id={}", id);
    }
}
