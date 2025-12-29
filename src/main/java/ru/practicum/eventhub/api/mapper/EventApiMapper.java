package ru.practicum.eventhub.api.mapper;

import org.mapstruct.Mapper;
import ru.practicum.eventhub.api.dto.request.EventCreateDto;
import ru.practicum.eventhub.api.dto.request.EventUpdateDto;
import ru.practicum.eventhub.api.dto.response.EventDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface EventApiMapper {

    EventCreateDto toServiceDto(ru.practicum.eventhub.api.model.EventCreateDto apiDto);

    EventUpdateDto toServiceDto(ru.practicum.eventhub.api.model.EventUpdateDto apiDto);

    ru.practicum.eventhub.api.model.EventDto toApiDto(EventDto serviceDto);

    default ru.practicum.eventhub.api.model.PagedEventDtoResponse toApiPage(PagedResponse<EventDto> page) {
        var apiPage = new ru.practicum.eventhub.api.model.PagedEventDtoResponse();

        apiPage.setContent(
                page.content().stream()
                        .map(this::toApiDto)
                        .toList()
        );
        apiPage.setPage(page.page());
        apiPage.setSize(page.size());
        apiPage.setTotalElements(page.totalElements());
        apiPage.setTotalPages(page.totalPages());

        return apiPage;
    }
}
