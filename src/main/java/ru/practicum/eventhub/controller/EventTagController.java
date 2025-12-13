package ru.practicum.eventhub.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventhub.api.dto.request.EventCreateDto;
import ru.practicum.eventhub.api.dto.request.EventUpdateDto;
import ru.practicum.eventhub.api.dto.request.TagCreateDto;
import ru.practicum.eventhub.api.dto.request.TagUpdateDto;
import ru.practicum.eventhub.api.dto.response.EventDto;
import ru.practicum.eventhub.api.dto.response.TagDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.service.EventService;
import ru.practicum.eventhub.domain.service.TagService;

import java.util.UUID;

@RestController
@RequestMapping("/events/v1/api")
@Validated
@RequiredArgsConstructor
public class EventTagController {
    private final EventService eventService;
    private final TagService tagService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(@Valid @RequestBody EventCreateDto dto) {
        return eventService.createEvent(dto);
    }

    @GetMapping(params = "!userId")
    public PagedResponse<EventDto> getEvents(@RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventService.getEvents(PageRequest.of(page, size));
    }

    @GetMapping
    public PagedResponse<EventDto> getEventsByUser(@RequestParam UUID userId,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
                                                   @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventService.getEventsByUser(userId, PageRequest.of(page, size));
    }

    @GetMapping("/{eventId}")
    public EventDto getEventById(@PathVariable UUID eventId) {
        return eventService.getEventById(eventId);
    }

    @PatchMapping("/{eventId}")
    public EventDto updateEvent(@PathVariable UUID eventId, @Valid @RequestBody EventUpdateDto dto) {
        return eventService.updateEvent(eventId, dto);
    }

    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);
    }

    @PostMapping("/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto createTag(@Valid @RequestBody TagCreateDto dto) {
        return tagService.createTag(dto);
    }

    @GetMapping("/tags")
    public PagedResponse<TagDto> getTags(@RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        return tagService.getTags(PageRequest.of(page, size));
    }

    @GetMapping("/{eventId}/tags")
    public PagedResponse<TagDto> getTagsByEvent(@PathVariable UUID eventId,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        return tagService.getTagsByEvent(eventId, PageRequest.of(page, size));
    }

    @GetMapping("/{eventId}/tags/{tagId}")
    public TagDto getTagByEvent(@PathVariable UUID eventId, @PathVariable UUID tagId) {
        return tagService.getTagByEvent(eventId, tagId);
    }

    @PatchMapping("/{eventId}/tags/{tagId}")
    public TagDto updateTagForEvent(@PathVariable UUID eventId,
                                    @PathVariable UUID tagId,
                                    @Valid @RequestBody TagUpdateDto dto) {
        return tagService.updateForEvent(eventId, tagId, dto);
    }

    @DeleteMapping("/{eventId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTagForEvent(@PathVariable UUID eventId,
                                  @PathVariable UUID tagId) {
        tagService.deleteForEvent(eventId, tagId);
    }
}
