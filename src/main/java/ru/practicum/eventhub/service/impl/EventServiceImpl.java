package ru.practicum.eventhub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.practicum.eventhub.api.dto.request.EventCreateDto;
import ru.practicum.eventhub.api.dto.request.EventUpdateDto;
import ru.practicum.eventhub.api.dto.response.EventDto;
import ru.practicum.eventhub.api.mapper.EventMapper;
import ru.practicum.eventhub.api.model.PageOfEvents;
import ru.practicum.eventhub.config.redis.ManyToManyCacheIndexService;
import ru.practicum.eventhub.model.Event;
import ru.practicum.eventhub.model.Tag;
import ru.practicum.eventhub.repository.EventRepository;
import ru.practicum.eventhub.service.EventService;
import ru.practicum.eventhub.service.cache.EventTagCacheService;
import ru.practicum.eventhub.util.PageValidator;
import ru.practicum.eventhub.validation.EventValidationService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private static final String EVENT_TAG_RELATION = "event_tag";

    private final EventRepository eventRepository;
    private final EventReadService eventReadService;
    private final EventMapper eventMapper;
    private final EventValidationService eventValidationService;
    private final ManyToManyCacheIndexService relationIndexService;
    private final EventTagCacheService tagCacheService;
    private final TransactionTemplate transactionTemplate;

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
    public PageOfEvents getEvents(Pageable pageable) {
        Page<Event> page = eventRepository.findAll(pageable);
        PageValidator.validatePage(page);

        log.info("Запрошены события: страница {}, размер {}", pageable.getPageNumber(), pageable.getPageSize());
        return toApiPage(page, eventMapper);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "events", key = "#id")
    public EventDto getEventById(UUID id) {
        Event event = eventReadService.findById(id);
        log.info("Запрошено событие с id={}", id);
        return eventMapper.toDto(event);
    }

    @Override
    @CachePut(value = "events", key = "#id")
    public EventDto updateEvent(UUID id, EventUpdateDto dto) {
        Map.Entry<Event, Set<UUID>> result = transactionTemplate.execute(status -> {
            Event eventEntity = eventReadService.findByIdForUpdate(id);

            eventValidationService.validateUpdate(dto);

            Set<UUID> oldTagIds = eventEntity.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
            eventEntity = eventMapper.updateEventFromDto(dto, eventEntity);
            eventEntity = eventRepository.save(eventEntity);

            Set<UUID> addedTagIds = eventEntity.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
            addedTagIds.removeAll(oldTagIds);
            return Map.entry(eventEntity, addedTagIds);
        });

        Event event = result.getKey();
        Set<UUID> addedTagIds = result.getValue();

        for (UUID tagId : addedTagIds) {
            relationIndexService.add(EVENT_TAG_RELATION, id, tagId);
        }
        tagCacheService.refreshByEventBatch(id, addedTagIds);

        log.info("Обновлено событие с id={}", id);
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional
    @CacheEvict(value = "events", key = "#id")
    public void deleteEvent(UUID id) {
        eventReadService.findById(id);
        eventRepository.deleteById(id);

        relationIndexService.deleteLeft(EVENT_TAG_RELATION, id);

        log.info("Удалено событие с id={}", id);
    }

    private PageOfEvents toApiPage(Page<Event> page, EventMapper mapper) {
        PageOfEvents apiPage = new PageOfEvents();
        apiPage.setContent(page.getContent().stream().map(mapper::toDto).toList());
        apiPage.setPageNumber(page.getNumber());
        apiPage.setPageSize(page.getSize());
        apiPage.setTotalElements(page.getTotalElements());
        apiPage.setTotalPages(page.getTotalPages());
        return apiPage;
    }
}
