package ru.practicum.eventhub.api.mapper;

import org.mapstruct.Mapper;
import ru.practicum.eventhub.api.dto.response.UserMetadataDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserMetadataApiMapper {

    ru.practicum.eventhub.api.model.UserMetadataDto toApiDto(UserMetadataDto serviceDto);

    default ru.practicum.eventhub.api.model.PagedUserMetadataDtoResponse toApiPage(PagedResponse<UserMetadataDto> page) {
        var apiPage = new ru.practicum.eventhub.api.model.PagedUserMetadataDtoResponse();

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
