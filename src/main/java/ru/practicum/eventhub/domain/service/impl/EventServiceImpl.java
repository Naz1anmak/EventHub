package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.request.EventCreateDto;
import ru.practicum.eventhub.api.dto.request.EventUpdateDto;
import ru.practicum.eventhub.api.dto.request.TagCreateDto;
import ru.practicum.eventhub.api.dto.response.EventDto;
import ru.practicum.eventhub.api.mapper.EventMapper;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.model.Event;
import ru.practicum.eventhub.domain.model.Tag;
import ru.practicum.eventhub.domain.repository.EventRepository;
import ru.practicum.eventhub.domain.repository.TagRepository;
import ru.practicum.eventhub.domain.service.EventService;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventReader eventReader;
    private final EventMapper eventMapper;
    private final TagReader tagReader;
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public EventDto createEvent(EventCreateDto dto) {
        Set<Tag> resultTags = getTags(dto);

        Event event = eventMapper.fromCreateDto(dto, resultTags);
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
    public EventDto getEventById(UUID id) {
        Event event = eventReader.findById(id);
        log.info("Запрошено событие с id={}", id);
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional
    public EventDto updateEvent(UUID id, EventUpdateDto dto) {
        Event event = eventReader.findById(id);

        if (dto.tags() != null) {
            Set<Tag> tags = dto.tags().isEmpty() ? Set.of() : tagReader.getTagsByIds(dto.tags());

            if (dto.tagUpdateMode() != null) {
                dto.tagUpdateMode().apply(event, tags);
            } else {
                event.setTags(tags);
            }
        }

        eventMapper.updateEventFromDto(dto, event);
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

    @Transactional
    public Set<Tag> getTags(EventCreateDto dto) {
        Set<String> names = dto.tags().stream()
                .map(TagCreateDto::name)
                .collect(Collectors.toSet());

        Map<String, Tag> existingTags = tagReader.findByNames(names);

        Set<Tag> resultTags = new HashSet<>();

        for (TagCreateDto tagDto : dto.tags()) {
            Tag tag = existingTags.get(tagDto.name());

            if (tag == null) {
                tag = Tag.create(tagDto);
                tagRepository.save(tag);
            }

            resultTags.add(tag);
        }
        return resultTags;
    }
}
