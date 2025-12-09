package ru.practicum.eventhub.api.controller;

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

    @GetMapping
    public PagedResponse<EventDto> getEvents(@RequestParam(required = false) UUID userId,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        if (userId != null) {
            return eventService.getEventsByUser(userId, PageRequest.of(page, size));
        }
        return eventService.getEvents(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public EventDto getEventById(@PathVariable UUID id) {
        return eventService.getEventById(id);
    }

    @PatchMapping("/{id}")
    public EventDto updateEvent(@PathVariable UUID id, @Valid @RequestBody EventUpdateDto dto) {
        return eventService.updateEvent(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable UUID id) {
        eventService.deleteEvent(id);
    }

    @PostMapping("/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto createTag(@Valid @RequestBody TagCreateDto dto) {
        return tagService.createTag(dto);
    }

    @GetMapping("/tags")
    public PagedResponse<TagDto> getTags(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        return tagService.getTags(PageRequest.of(page, size));
    }

    @GetMapping("/tags/{id}")
    public TagDto getTagById(@PathVariable UUID id) {
        return tagService.getTagById(id);
    }

    @PatchMapping("/tags/{id}")
    public TagDto updateTag(@PathVariable UUID id, @Valid @RequestBody TagUpdateDto dto) {
        return tagService.updateTag(id, dto);
    }

    @DeleteMapping("/tags/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
    }
}
