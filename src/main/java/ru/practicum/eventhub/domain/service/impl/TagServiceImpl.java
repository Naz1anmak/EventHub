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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

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
    public TagDto getTagById(UUID id) {
        Tag tag = getTagByIdOrThrow(id);
        log.info("Получен тег с id={}", id);
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional
    public TagDto updateTag(UUID id, TagUpdateDto dto) {
        Tag tag = getTagByIdOrThrow(id);
        tagMapper.updateTagFromDto(dto, tag);

        try {
            tag = tagRepository.save(tag);
        } catch (DataIntegrityViolationException exception) {
            log.error(exception.getMessage(), exception);
            throw new ConflictException("Тег с именем '" + dto.name() + "' уже существует.");
        }

        log.info("Обновлен тег с id={}", id);
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional
    public void deleteTag(UUID id) {
        Tag tag = getTagByIdOrThrow(id);
        tagRepository.delete(tag);
        log.info("Удален тег с id={}", id);
    }

    @Override
    public Tag getTagByIdOrThrow(UUID id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Тег с ID '" + id + "' не найден."));
    }

    @Override
    public Set<Tag> getTagsByIdsOrThrow(Set<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return Set.of();
        }

        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(ids));
        if (tags.size() != ids.size()) {
            Set<UUID> foundIds = tags.stream().map(Tag::getId).collect(Collectors.toSet());
            Set<UUID> missing = new HashSet<>(ids);
            missing.removeAll(foundIds);
            log.error("Не найдены теги c id={}", missing);
            throw new NotFoundException("Не найдены теги c id=" + missing);
        }

        return tags;
    }
}
