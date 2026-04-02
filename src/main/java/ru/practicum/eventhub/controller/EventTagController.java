package ru.practicum.eventhub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.eventhub.api.EventsApi;
import ru.practicum.eventhub.api.dto.request.EventCreateDto;
import ru.practicum.eventhub.api.dto.request.EventUpdateDto;
import ru.practicum.eventhub.api.dto.request.TagCreateDto;
import ru.practicum.eventhub.api.dto.request.TagUpdateDto;
import ru.practicum.eventhub.api.dto.response.EventDto;
import ru.practicum.eventhub.api.dto.response.TagDto;
import ru.practicum.eventhub.api.dto.response.TagWithStatsDto;
import ru.practicum.eventhub.api.model.PageOfEvents;
import ru.practicum.eventhub.api.model.PageOfTags;
import ru.practicum.eventhub.service.EventService;
import ru.practicum.eventhub.service.TagService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EventTagController implements EventsApi {
    private final EventService eventService;
    private final TagService tagService;

    @Override
    public ResponseEntity<TagWithStatsDto> addTagToEvent(UUID eventId, UUID tagId) {
        TagWithStatsDto tagWithStatsDto = tagService.addTagToEvent(eventId, tagId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tagWithStatsDto);
    }

    @Override
    public ResponseEntity<EventDto> createEvent(EventCreateDto eventCreateDto) {
        EventDto eventDto = eventService.createEvent(eventCreateDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventDto);
    }

    @Override
    public ResponseEntity<TagDto> createTag(TagCreateDto tagCreateDto) {
        TagDto tagDto = tagService.createTag(tagCreateDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tagDto);
    }

    @Override
    public ResponseEntity<Void> deleteEvent(UUID eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Override
    public ResponseEntity<Void> deleteTag(UUID tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Override
    public ResponseEntity<Void> deleteTagForEvent(UUID eventId, UUID tagId) {
        tagService.deleteForEvent(eventId, tagId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Override
    public ResponseEntity<EventDto> getEventById(UUID eventId) {
        EventDto eventDto = eventService.getEventById(eventId);
        return ResponseEntity.ok(eventDto);
    }

    @Override
    public ResponseEntity<PageOfEvents> getEvents(Integer page, Integer size) {
        PageOfEvents pageOfEvents = eventService.getEvents(PageRequest.of(page, size));
        return ResponseEntity.ok(pageOfEvents);
    }

    @Override
    public ResponseEntity<TagWithStatsDto> getTagByEventId(UUID eventId, UUID tagId) {
        TagWithStatsDto tagDto = tagService.getTagByEvent(eventId, tagId);
        return ResponseEntity.ok(tagDto);
    }

    @Override
    public ResponseEntity<PageOfTags> getTags(Integer page, Integer size) {
        PageOfTags pageOfTags = tagService.getTags(PageRequest.of(page, size));
        return ResponseEntity.ok(pageOfTags);
    }

    @Override
    public ResponseEntity<PageOfTags> getTagsByEventId(UUID eventId, Integer page, Integer size) {
        PageOfTags pageOfTags = tagService.getTagsByEvent(eventId, PageRequest.of(page, size));
        return ResponseEntity.ok(pageOfTags);
    }

    @Override
    public ResponseEntity<EventDto> updateEvent(UUID eventId, EventUpdateDto eventUpdateDto) {
        EventDto eventDto = eventService.updateEvent(eventId, eventUpdateDto);
        return ResponseEntity.ok(eventDto);
    }

    @Override
    public ResponseEntity<TagDto> updateTag(UUID tagId, TagUpdateDto tagUpdateDto) {
        TagDto tagDto = tagService.updateTag(tagId, tagUpdateDto);
        return ResponseEntity.ok(tagDto);
    }
}
