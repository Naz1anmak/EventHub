package ru.practicum.eventhub.api.mapper;

import org.mapstruct.Mapper;
import ru.practicum.eventhub.api.dto.request.ProjectUpdateDto;
import ru.practicum.eventhub.api.dto.response.ProjectDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ProjectApiMapper {

    ProjectUpdateDto toServiceDto(ru.practicum.eventhub.api.model.ProjectUpdateDto apiDto);

    ru.practicum.eventhub.api.model.ProjectDto toApiDto(ProjectDto serviceDto);

    default ru.practicum.eventhub.api.model.PagedProjectDtoResponse toApiPage(PagedResponse<ProjectDto> page) {
        var apiPage = new ru.practicum.eventhub.api.model.PagedProjectDtoResponse();

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
