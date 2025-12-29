package ru.practicum.eventhub.api.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.practicum.eventhub.api.dto.request.ProjectUpdateDto;
import ru.practicum.eventhub.api.dto.response.ProjectDto;
import ru.practicum.eventhub.domain.model.Project;
import ru.practicum.eventhub.domain.model.User;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = SPRING)
public interface ProjectMapper {
    ProjectDto toDto(Project project);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "name", source = "updateDto.name")
    @Mapping(target = "description", source = "updateDto.description")
    @Mapping(target = "owner", source = "owner")
    Project updateProjectFromDto(ProjectUpdateDto updateDto, @MappingTarget Project project, User owner);
}
