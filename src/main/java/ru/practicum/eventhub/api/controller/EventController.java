package ru.practicum.eventhub.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventhub.api.dto.request.EventCreateDto;
import ru.practicum.eventhub.api.dto.request.EventUpdateDto;
import ru.practicum.eventhub.api.dto.response.EventDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.service.EventService;

import java.util.UUID;

@RestController
@RequestMapping("/events")
@Validated
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(@Valid @RequestBody EventCreateDto dto) {
        return eventService.createEvent(dto);
    }

    @GetMapping
    public PagedResponse<EventDto> getEvents(@RequestParam(required = false) UUID userId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
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
}
