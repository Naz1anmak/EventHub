package ru.practicum.eventhub.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.eventhub.api.dto.request.TagCreateDto;
import ru.practicum.eventhub.api.dto.request.TagUpdateDto;
import ru.practicum.eventhub.api.dto.response.TagDto;
import ru.practicum.eventhub.api.dto.response.TagWithStatsDto;
import ru.practicum.eventhub.api.model.PageOfTags;

import java.util.UUID;

public interface TagService {
    TagWithStatsDto addTagToEvent(UUID eventId, UUID tagId);

    TagDto createTag(TagCreateDto dto);

    PageOfTags getTags(Pageable pageable);

    PageOfTags getTagsByEvent(UUID eventId, Pageable pageable);

    TagWithStatsDto getTagByEvent(UUID eventId, UUID tagId);

    TagDto updateTag(UUID tagId, TagUpdateDto dto);

    void deleteForEvent(UUID eventId, UUID tagId);

    void deleteTag(UUID tagId);

    void cacheRefreshAfterAddTag(UUID eventId, UUID tagId);
}
