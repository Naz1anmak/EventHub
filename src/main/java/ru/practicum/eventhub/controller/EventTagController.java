package ru.practicum.eventhub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.eventhub.api.EventsApi;
import ru.practicum.eventhub.api.mapper.EventApiMapper;
import ru.practicum.eventhub.api.mapper.TagApiMapper;
import ru.practicum.eventhub.api.model.*;
import ru.practicum.eventhub.domain.service.EventService;
import ru.practicum.eventhub.domain.service.TagService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EventTagController implements EventsApi {
    private final EventService eventService;
    private final TagService tagService;
    private final EventApiMapper eventApiMapper;
    private final TagApiMapper tagApiMapper;

    @Override
    public ResponseEntity<TagDto> addTagToEvent(UUID eventId, UUID tagId) {
        var serviceDto = tagService.addTagToEvent(eventId, tagId);
        var apiDto = tagApiMapper.toApiDto(serviceDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiDto);
    }

    @Override
    public ResponseEntity<EventDto> createEvent(EventCreateDto eventCreateDto) {
        var serviceCreateDto = eventApiMapper.toServiceDto(eventCreateDto);
        var serviceResult = eventService.createEvent(serviceCreateDto);
        var apiResult = eventApiMapper.toApiDto(serviceResult);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResult);
    }

    @Override
    public ResponseEntity<TagDto> createTag(TagCreateDto tagCreateDto) {
        var serviceCreateDto = tagApiMapper.toServiceDto(tagCreateDto);
        var serviceResult = tagService.createTag(serviceCreateDto);
        var apiResult = tagApiMapper.toApiDto(serviceResult);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResult);
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
        var serviceDto = eventService.getEventById(eventId);
        return ResponseEntity.ok(eventApiMapper.toApiDto(serviceDto));
    }

    @Override
    public ResponseEntity<PagedEventDtoResponse> getEvents(Integer page, Integer size) {
        var servicePage = eventService.getEvents(PageRequest.of(page, size));
        return ResponseEntity.ok(eventApiMapper.toApiPage(servicePage));
    }

    @Override
    public ResponseEntity<TagDto> getTagByEventId(UUID eventId, UUID tagId) {
        var serviceDto = tagService.getTagByEvent(eventId, tagId);
        return ResponseEntity.ok(tagApiMapper.toApiDto(serviceDto));
    }

    @Override
    public ResponseEntity<PagedTagDtoResponse> getTags(Integer page, Integer size) {
        var servicePage = tagService.getTags(PageRequest.of(page, size));
        return ResponseEntity.ok(tagApiMapper.toApiPage(servicePage));
    }

    @Override
    public ResponseEntity<PagedTagDtoResponse> getTagsByEventId(UUID eventId, Integer page, Integer size) {
        var servicePage = tagService.getTagsByEvent(eventId, PageRequest.of(page, size));
        return ResponseEntity.ok(tagApiMapper.toApiPage(servicePage));
    }

    @Override
    public ResponseEntity<EventDto> updateEvent(UUID eventId, EventUpdateDto eventUpdateDto) {
        var serviceUpdateDto = eventApiMapper.toServiceDto(eventUpdateDto);
        var serviceResult = eventService.updateEvent(eventId, serviceUpdateDto);
        var apiResult = eventApiMapper.toApiDto(serviceResult);

        return ResponseEntity.ok(apiResult);
    }

    @Override
    public ResponseEntity<TagDto> updateTag(UUID tagId, TagUpdateDto tagUpdateDto) {
        var serviceUpdateDto = tagApiMapper.toServiceDto(tagUpdateDto);
        var serviceResult = tagService.updateTag(tagId, serviceUpdateDto);
        var apiResult = tagApiMapper.toApiDto(serviceResult);

        return ResponseEntity.ok(apiResult);
    }
}
