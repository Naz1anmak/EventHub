package ru.practicum.eventhub.application.openapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.eventhub.api.dto.response.EventDto;
import ru.practicum.eventhub.api.model.PageOfEvents;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.service.EventService;

@Service
@RequiredArgsConstructor
public class EventPageMapper {
    private final EventService eventService;

    public PageOfEvents getEvents(Pageable pageable) {
        PagedResponse<EventDto> page = eventService.getEvents(pageable);
        return getPageOfEvents(page);
    }

    private static @NonNull PageOfEvents getPageOfEvents(PagedResponse<EventDto> page) {
        PageOfEvents apiPage = new PageOfEvents();
        apiPage.setPage(page.page());
        apiPage.setSize(page.size());
        apiPage.setTotalElements(page.totalElements());
        apiPage.setTotalPages(page.totalPages());
        apiPage.setContent(page.content());
        return apiPage;
    }
}
