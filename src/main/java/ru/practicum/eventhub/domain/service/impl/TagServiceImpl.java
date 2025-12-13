package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.request.TagCreateDto;
import ru.practicum.eventhub.api.dto.request.TagUpdateDto;
import ru.practicum.eventhub.api.dto.response.TagDto;
import ru.practicum.eventhub.api.exception.ConflictException;
import ru.practicum.eventhub.api.exception.NotFoundException;
import ru.practicum.eventhub.api.mapper.TagMapper;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.model.Tag;
import ru.practicum.eventhub.domain.repository.TagRepository;
import ru.practicum.eventhub.domain.service.TagService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final TagReader tagReader;

    @Override
    @Transactional
    public TagDto createTag(TagCreateDto dto) {
        Tag tag = tagMapper.fromCreateDto(dto);

        try {
            tag = tagRepository.save(tag);
        } catch (DataIntegrityViolationException exception) {
            log.error(exception.getMessage(), exception);
            throw new ConflictException("Тег с именем '" + dto.name() + "' уже существует.");
        }

        log.info("Создан тег с id={}", tag.getId());
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<TagDto> getTags(Pageable pageable) {
        Page<Tag> tagPage = tagRepository.findAll(pageable);

        log.info("Запрошены теги: страница {}, размер {}", pageable.getPageNumber(), pageable.getPageSize());
        return PagedResponse.from(tagPage, tagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<TagDto> getTagsByEvent(UUID eventId, Pageable pageable) {
        Page<Tag> tagPage = tagRepository.findAllByEventsId(eventId, pageable);
        if (tagPage.isEmpty()) {
            log.error("Не найдены теги для события с id={}", eventId);
            throw new NotFoundException("Не найдены теги для события с id=" + eventId);
        }

        log.info("Запрошены теги для события с id={}: страница {}, размер {}",
                eventId, pageable.getPageNumber(), pageable.getPageSize());
        return PagedResponse.from(tagPage, tagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public TagDto getTagByEvent(UUID eventId, UUID tagId) {
        Tag tag = tagReader.findByIdAndEventsId(tagId, eventId);
        log.info("Запрошен тег с id={} для события с id={}", tagId, eventId);
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional
    public TagDto updateForEvent(UUID eventId, UUID tagId, TagUpdateDto dto) {
        Tag tag = tagReader.findByIdAndEventsId(tagId, eventId);
        tagMapper.updateTagFromDto(dto, tag);

        try {
            tag = tagRepository.save(tag);
        } catch (DataIntegrityViolationException exception) {
            log.error(exception.getMessage(), exception);
            throw new ConflictException("Тег с именем '" + dto.name() + "' уже существует.");
        }

        log.info("Обновлен тег с id={} для события с id={}", tagId, eventId);
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional
    public void deleteForEvent(UUID eventId, UUID tagId) {
        Tag tag = tagReader.findByIdAndEventsId(tagId, eventId);
        tagRepository.delete(tag);
        log.info("Удален тег с id={} для события с id={}", tagId, eventId);
    }
}
