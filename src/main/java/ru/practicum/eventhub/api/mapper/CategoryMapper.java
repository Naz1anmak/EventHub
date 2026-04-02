package ru.practicum.eventhub.api.mapper;

import org.mapstruct.*;
import ru.practicum.eventhub.api.dto.request.CategoryCreateDto;
import ru.practicum.eventhub.api.dto.request.CategoryUpdateDto;
import ru.practicum.eventhub.api.dto.request.ProjectCreateDto;
import ru.practicum.eventhub.api.dto.response.CategoryDto;
import ru.practicum.eventhub.model.Category;
import ru.practicum.eventhub.model.Project;

import java.util.Set;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = SPRING)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "projects", ignore = true)
    Category fromCreateDto(CategoryCreateDto dto);

    @AfterMapping
    default void afterCreate(CategoryCreateDto dto, @MappingTarget Category category) {
        addProjectsInternal(dto.projects(), category);
    }

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "projects", ignore = true)
    Category updateCategoryFromDto(CategoryUpdateDto dto, @MappingTarget Category category);

    @AfterMapping
    default void afterUpdate(CategoryUpdateDto dto, @MappingTarget Category category) {
        addProjectsInternal(dto.projects(), category);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Project projectFromNestedDto(ProjectCreateDto dto);

    private void addProjectsInternal(Set<ProjectCreateDto> projects, Category category) {
        if (projects == null) {
            return;
        }
        for (ProjectCreateDto p : projects) {
            Project project = projectFromNestedDto(p);
            category.addProject(project);
        }
    }
}
