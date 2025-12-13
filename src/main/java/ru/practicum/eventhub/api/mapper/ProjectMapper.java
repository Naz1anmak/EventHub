package ru.practicum.eventhub.api.mapper;

import org.mapstruct.*;
import ru.practicum.eventhub.api.dto.request.ProjectCreateDto;
import ru.practicum.eventhub.api.dto.request.ProjectUpdateDto;
import ru.practicum.eventhub.api.dto.response.ProjectDto;
import ru.practicum.eventhub.domain.model.Category;
import ru.practicum.eventhub.domain.model.Project;
import ru.practicum.eventhub.domain.model.User;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ProjectMapper {
    ProjectDto toDto(Project project);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "name", source = "createDto.name")
    @Mapping(target = "description", source = "createDto.description")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "owner", source = "owner")
    Project fromCreateDto(ProjectCreateDto createDto, Category category, User owner);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "name", source = "updateDto.name")
    @Mapping(target = "description", source = "updateDto.description")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "owner", source = "owner")
    void updateProjectFromDto(ProjectUpdateDto updateDto, @MappingTarget Project project, Category category, User owner);
}
