package ru.practicum.eventhub.api.mapper;

import org.mapstruct.*;
import ru.practicum.eventhub.api.dto.request.CategoryCreateDto;
import ru.practicum.eventhub.api.dto.request.CategoryUpdateDto;
import ru.practicum.eventhub.api.dto.request.ProjectCreateDto;
import ru.practicum.eventhub.api.dto.response.CategoryDto;
import ru.practicum.eventhub.domain.model.Category;
import ru.practicum.eventhub.domain.model.Project;
import ru.practicum.eventhub.domain.model.User;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = SPRING)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "projects", ignore = true)
    Category fromCreateDto(CategoryCreateDto dto, @Context Map<UUID, User> owners);

    @AfterMapping
    default void afterCreate(CategoryCreateDto dto, @MappingTarget Category category, @Context Map<UUID, User> owners) {
        addProjectsInternal(dto.projects(), category, owners);
    }

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "projects", ignore = true)
    Category updateCategoryFromDto(CategoryUpdateDto dto, @MappingTarget Category category, @Context Map<UUID, User> owners);

    @AfterMapping
    default void afterUpdate(CategoryUpdateDto dto, @MappingTarget Category category, @Context Map<UUID, User> owners) {
        addProjectsInternal(dto.projects(), category, owners);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Project projectFromNestedDto(ProjectCreateDto dto);

    private void addProjectsInternal(Set<ProjectCreateDto> projects, Category category, Map<UUID, User> owners) {
        if (projects == null) {
            return;
        }
        for (ProjectCreateDto p : projects) {
            Project project = projectFromNestedDto(p);
            project.setOwner(owners.get(p.ownerId()));
            category.addProject(project);
        }
    }
}
