package ru.practicum.eventhub.domain.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.eventhub.api.dto.request.TagCreateDto;
import ru.practicum.eventhub.api.dto.request.TagUpdateDto;
import ru.practicum.eventhub.api.dto.response.TagDto;
import ru.practicum.eventhub.api.dto.response.TagWithStatsDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;

import java.util.UUID;

public interface TagService {
    TagDto createTag(TagCreateDto dto);

    TagDto addTagToEvent(UUID eventId, UUID tagId);

    PagedResponse<TagDto> getTags(Pageable pageable);

    PagedResponse<TagDto> getTagsByEvent(UUID eventId, Pageable pageable);

    TagWithStatsDto getTagByEvent(UUID eventId, UUID tagId);

    TagDto updateTag(UUID tagId, TagUpdateDto dto);

    void deleteForEvent(UUID eventId, UUID tagId);

    void deleteTag(UUID tagId);
}
