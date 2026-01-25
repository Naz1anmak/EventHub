package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.request.TagCreateDto;
import ru.practicum.eventhub.api.dto.request.TagUpdateDto;
import ru.practicum.eventhub.api.dto.response.TagDto;
import ru.practicum.eventhub.api.exception.ConflictException;
import ru.practicum.eventhub.api.mapper.TagMapper;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.model.Event;
import ru.practicum.eventhub.domain.model.Tag;
import ru.practicum.eventhub.domain.repository.TagRepository;
import ru.practicum.eventhub.domain.service.TagService;
import ru.practicum.eventhub.domain.util.PageValidator;
import ru.practicum.eventhub.domain.validation.TagValidationService;

import java.util.Iterator;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final TagReadService tagReadService;
    private final EventReadService eventReadService;
    private final TagValidationService tagValidationService;

    @Override
    @Transactional
    public TagDto createTag(TagCreateDto dto) {
        tagValidationService.validateCreate(dto);
        Tag tag = tagMapper.fromCreateDto(dto);

        tag = tagRepository.save(tag);
        log.info("Создан тег с id={}", tag.getId());
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional
    public TagDto addTagToEvent(UUID eventId, UUID tagId) {
        Event event = eventReadService.findById(eventId);
        Tag tag = tagReadService.findById(tagId);

        if (tagReadService.existsByIdAndEventsId(tagId, eventId)) {
            log.error("Тег с id={} уже добавлен к событию с id={}", tagId, eventId);
            throw new ConflictException("Тег с id=" + tagId + " уже добавлен к событию с id=" + eventId);
        }

        event.addTag(tag);
        log.info("Тег с id={} добавлен к событию с id={}", tagId, eventId);
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<TagDto> getTags(Pageable pageable) {
        Page<Tag> page = tagRepository.findAll(pageable);
        PageValidator.validatePage(page);

        log.info("Запрошены теги: страница {}, размер {}", pageable.getPageNumber(), pageable.getPageSize());
        return PagedResponse.from(page, tagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<TagDto> getTagsByEvent(UUID eventId, Pageable pageable) {
        eventReadService.findById(eventId);
        Page<Tag> page = tagRepository.findAllByEventsId(eventId, pageable);
        PageValidator.validatePage(page);

        log.info("Запрошены теги для события с id={}: страница {}, размер {}",
                eventId, pageable.getPageNumber(), pageable.getPageSize());
        return PagedResponse.from(page, tagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public TagDto getTagByEvent(UUID eventId, UUID tagId) {
        eventReadService.findById(eventId);
        Tag tag = tagReadService.findByIdAndEventsId(tagId, eventId);
        log.info("Запрошен тег с id={} для события с id={}", tagId, eventId);
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional
    public TagDto updateTag(UUID tagId, TagUpdateDto dto) {
        Tag tag = tagReadService.findById(tagId);

        tagValidationService.validateUpdate(dto, tag);

        tag = tagMapper.updateTagFromDto(dto, tag);

        tag = tagRepository.save(tag);
        log.info("Обновлен тег с id={}", tagId);
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional
    public void deleteForEvent(UUID eventId, UUID tagId) {
        Event event = eventReadService.findById(eventId);
        Tag tag = tagReadService.findByIdAndEventsId(tagId, eventId);

        event.removeTag(tag);
        log.info("Тег с id={} отвязан от события с id={}", tagId, eventId);
    }

    @Override
    @Transactional
    public void deleteTag(UUID tagId) {
        Tag tag = tagReadService.findById(tagId);

        Iterator<Event> iterator = tag.getEvents().iterator();
        while (iterator.hasNext()) {
            Event event = iterator.next();
            iterator.remove();
            event.getTags().remove(tag);
        }

        tagRepository.deleteById(tagId);
        log.info("Удален тег с id={}", tagId);
    }
}
