package ru.practicum.eventhub.domain.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.eventhub.api.dto.request.EventCreateDto;
import ru.practicum.eventhub.api.dto.request.EventUpdateDto;
import ru.practicum.eventhub.api.dto.response.EventDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;

import java.util.UUID;

public interface EventService {
    EventDto createEvent(EventCreateDto dto);

    PagedResponse<EventDto> getEvents(Pageable pageable);

    EventDto getEventById(UUID id);

    EventDto updateEvent(UUID id, EventUpdateDto dto);

    void deleteEvent(UUID id);
}
