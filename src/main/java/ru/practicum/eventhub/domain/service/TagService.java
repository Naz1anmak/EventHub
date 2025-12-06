package ru.practicum.eventhub.domain.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.eventhub.api.dto.request.TagCreateDto;
import ru.practicum.eventhub.api.dto.request.TagUpdateDto;
import ru.practicum.eventhub.api.dto.response.TagDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.model.Tag;

import java.util.UUID;

public interface TagService {
    TagDto createTag(TagCreateDto dto);

    PagedResponse<TagDto> getTags(Pageable pageable);

    TagDto getTagById(UUID id);

    TagDto updateTag(UUID id, TagUpdateDto dto);

    void deleteTag(UUID id);

    Tag getTagByIdOrThrow(UUID id);
}
