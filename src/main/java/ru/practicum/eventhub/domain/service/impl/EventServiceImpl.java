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
import ru.practicum.eventhub.api.exception.types.NotFoundException;
import ru.practicum.eventhub.api.mapper.EventMapper;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.model.Category;
import ru.practicum.eventhub.domain.model.Event;
import ru.practicum.eventhub.domain.model.Tag;
import ru.practicum.eventhub.domain.model.User;
import ru.practicum.eventhub.domain.repository.EventRepository;
import ru.practicum.eventhub.domain.service.CategoryService;
import ru.practicum.eventhub.domain.service.EventService;
import ru.practicum.eventhub.domain.service.TagService;
import ru.practicum.eventhub.domain.service.UserService;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryService categoryService;
    private final UserService userService;
    private final TagService tagService;

    @Override
    @Transactional
    public EventDto createEvent(EventCreateDto dto) {
        Category category = categoryService.getCategoryByIdOrThrow(dto.categoryId());
        User user = userService.getUserByIdOrThrow(dto.createdBy());

        Set<Tag> tags = Set.of();
        if (dto.tags() != null && !dto.tags().isEmpty()) {
            tags = tagService.getTagsByIdsOrThrow(dto.tags());
        }

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
        User user = userService.getUserByIdOrThrow(userId);
        Page<Event> eventPage = eventRepository.findByCreatedBy(user, pageable);

        log.info("Запрошены события пользователя {}: страница {}, размер {}",
                userId, pageable.getPageNumber(), pageable.getPageSize());
        return PagedResponse.from(eventPage, eventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto getEventById(UUID id) {
        Event event = getEventByIdOrThrow(id);
        log.info("Запрошено событие с id={}", id);
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional
    public EventDto updateEvent(UUID id, EventUpdateDto dto) {
        Event event = getEventByIdOrThrow(id);

        Category category = null;
        if (dto.categoryId() != null) {
            category = categoryService.getCategoryByIdOrThrow(dto.categoryId());
        }

        User user = null;
        if (dto.createdBy() != null) {
            user = userService.getUserByIdOrThrow(dto.createdBy());
        }

        if (dto.tags() != null) {
            Set<Tag> tags = dto.tags().isEmpty() ? Set.of() : tagService.getTagsByIdsOrThrow(dto.tags());

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
        getEventByIdOrThrow(id);
        eventRepository.deleteById(id);
        log.info("Удалено событие с id={}", id);
    }

    public Event getEventByIdOrThrow(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + id + " не найдено"));
    }
}
