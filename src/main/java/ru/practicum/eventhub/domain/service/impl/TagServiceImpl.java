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
import ru.practicum.eventhub.api.mapper.TagMapper;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.exception.ConflictException;
import ru.practicum.eventhub.domain.exception.NotFoundException;
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

    @Override
    @Transactional
    public TagDto createTag(TagCreateDto dto) {
        Tag tag = tagMapper.fromCreateDto(dto);

        try {
            tag = tagRepository.save(tag);
        } catch (DataIntegrityViolationException exception) {
            throw new ConflictException("Тег с именем '" + dto.name() + "' уже существует.");
        }

        log.info("Создан тег: {}", tag);
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<TagDto> getTags(Pageable pageable) {
        Page<Tag> tagPage = tagRepository.findAll(pageable);

        log.info("Запрошены теги: страница {}, размер {}", pageable.getPageNumber(), pageable.getPageSize());
        return new PagedResponse<>(
                tagPage.getContent().stream().map(tagMapper::toDto).toList(),
                tagPage.getNumber(),
                tagPage.getSize(),
                tagPage.getTotalElements(),
                tagPage.getTotalPages()
        );
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
                .orElseThrow(() -> new NotFoundException("Тег с ID '" + id + "' не найден.")
                );
    }
}
