package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.request.EventCreateDto;
import ru.practicum.eventhub.api.dto.request.EventUpdateDto;
import ru.practicum.eventhub.api.dto.response.EventDto;
import ru.practicum.eventhub.api.mapper.EventMapper;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.model.Category;
import ru.practicum.eventhub.domain.model.Event;
import ru.practicum.eventhub.domain.model.Tag;
import ru.practicum.eventhub.domain.model.User;
import ru.practicum.eventhub.domain.repository.EventRepository;
import ru.practicum.eventhub.domain.service.EventService;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventReader eventReader;
    private final EventMapper eventMapper;
    private final CategoryReader categoryReader;
    private final UserReader userReader;
    private final TagReader tagReader;

    @Override
    @Transactional
    public EventDto createEvent(EventCreateDto dto) {
        Category category = categoryReader.findById(dto.categoryId());
        User user = userReader.findById(dto.createdBy());
        Set<Tag> tags = tagReader.getTagsByIds(dto.tags());

        Event event = eventMapper.fromCreateDto(dto, category, user, tags);
        event = eventRepository.save(event);

        log.info("Создано событие с id={}", event.getId());
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<EventDto> getEvents(Pageable pageable) {
        Page<Event> eventPage = eventRepository.findAll(pageable);

        log.info("Запрошены события: страница {}, размер {}", pageable.getPageNumber(), pageable.getPageSize());
        return PagedResponse.from(eventPage, eventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<EventDto> getEventsByUser(UUID userId, Pageable pageable) {
        User user = userReader.findById(userId);
        Page<Event> eventPage = eventRepository.findByCreatedBy(user, pageable);

        log.info("Запрошены события пользователя {}: страница {}, размер {}",
                userId, pageable.getPageNumber(), pageable.getPageSize());
        return PagedResponse.from(eventPage, eventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto getEventById(UUID id) {
        Event event = eventReader.findById(id);
        log.info("Запрошено событие с id={}", id);
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional
    public EventDto updateEvent(UUID id, EventUpdateDto dto) {
        Event event = eventReader.findById(id);

        Category category = null;
        if (dto.categoryId() != null) {
            category = categoryReader.findById(dto.categoryId());
        }

        User user = null;
        if (dto.createdBy() != null) {
            user = userReader.findById(dto.createdBy());
        }

        if (dto.tags() != null) {
            Set<Tag> tags = dto.tags().isEmpty() ? Set.of() : tagReader.getTagsByIds(dto.tags());

            if (dto.tagUpdateMode() != null) {
                dto.tagUpdateMode().apply(event, tags);
            } else {
                event.setTags(tags);
            }
        }

        eventMapper.updateEventFromDto(dto, event, category, user);
        event = eventRepository.save(event);

        log.info("Обновлено событие с id={}", id);
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional
    public void deleteEvent(UUID id) {
        eventReader.findById(id);
        eventRepository.deleteById(id);
        log.info("Удалено событие с id={}", id);
    }
}
