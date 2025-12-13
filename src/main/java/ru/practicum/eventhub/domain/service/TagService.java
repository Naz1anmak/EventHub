package ru.practicum.eventhub.domain.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.eventhub.api.dto.request.TagCreateDto;
import ru.practicum.eventhub.api.dto.request.TagUpdateDto;
import ru.practicum.eventhub.api.dto.response.TagDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;

import java.util.UUID;

public interface TagService {
    TagDto createTag(TagCreateDto dto);

    PagedResponse<TagDto> getTags(Pageable pageable);

    PagedResponse<TagDto> getTagsByEvent(UUID eventId, Pageable pageable);

    TagDto getTagByEvent(UUID eventId, UUID tagId);

    TagDto updateForEvent(UUID eventId, UUID tagId, TagUpdateDto dto);

    void deleteForEvent(UUID eventId, UUID tagId);
}
