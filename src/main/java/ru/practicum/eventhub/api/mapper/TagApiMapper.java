package ru.practicum.eventhub.api.mapper;

import org.mapstruct.Mapper;
import ru.practicum.eventhub.api.dto.request.TagCreateDto;
import ru.practicum.eventhub.api.dto.request.TagUpdateDto;
import ru.practicum.eventhub.api.dto.response.TagDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface TagApiMapper {

    TagCreateDto toServiceDto(ru.practicum.eventhub.api.model.TagCreateDto apiDto);

    TagUpdateDto toServiceDto(ru.practicum.eventhub.api.model.TagUpdateDto apiDto);

    ru.practicum.eventhub.api.model.TagDto toApiDto(TagDto serviceDto);

    default ru.practicum.eventhub.api.model.PagedTagDtoResponse toApiPage(PagedResponse<TagDto> page) {
        var apiPage = new ru.practicum.eventhub.api.model.PagedTagDtoResponse();

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
