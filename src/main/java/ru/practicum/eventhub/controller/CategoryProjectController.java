package ru.practicum.eventhub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.eventhub.api.CategoriesApi;
import ru.practicum.eventhub.api.dto.request.CategoryCreateDto;
import ru.practicum.eventhub.api.dto.request.CategoryUpdateDto;
import ru.practicum.eventhub.api.dto.request.ProjectUpdateDto;
import ru.practicum.eventhub.api.dto.response.CategoryDto;
import ru.practicum.eventhub.api.dto.response.ProjectDto;
import ru.practicum.eventhub.api.model.PageOfCategories;
import ru.practicum.eventhub.api.model.PageOfProjects;
import ru.practicum.eventhub.application.openapi.CategoryApplicationService;
import ru.practicum.eventhub.application.openapi.ProjectApplicationService;
import ru.practicum.eventhub.domain.service.CategoryService;
import ru.practicum.eventhub.domain.service.ProjectService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CategoryProjectController implements CategoriesApi {
    private final CategoryApplicationService categoryApplicationService;
    private final ProjectApplicationService projectApplicationService;
    private final CategoryService categoryService;
    private final ProjectService projectService;

    @Override
    public ResponseEntity<CategoryDto> createCategory(CategoryCreateDto categoryCreateDto) {
        CategoryDto categoryDto = categoryService.createCategory(categoryCreateDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryDto);
    }

    @Override
    public ResponseEntity<Void> deleteCategory(UUID categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Override
    public ResponseEntity<Void> deleteProjectForCategory(UUID categoryId, UUID projectId) {
        projectService.deleteForCategory(categoryId, projectId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Override
    public ResponseEntity<PageOfCategories> getCategories(Integer page, Integer size) {
        PageOfCategories pageOfCategories = categoryApplicationService.getCategories(PageRequest.of(page, size));
        return ResponseEntity.ok(pageOfCategories);
    }

    @Override
    public ResponseEntity<CategoryDto> getCategoryById(UUID categoryId) {
        CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(categoryDto);
    }

    @Override
    public ResponseEntity<ProjectDto> getProjectByCategoryId(UUID categoryId, UUID projectId) {
        ProjectDto projectDto = projectService.getProjectByCategory(categoryId, projectId);
        return ResponseEntity.ok(projectDto);
    }

    @Override
    public ResponseEntity<PageOfProjects> getProjectsByCategoryId(UUID categoryId, Integer page, Integer size) {
        PageOfProjects pageOfProjects = projectApplicationService.getProjectsByCategory(categoryId, PageRequest.of(page, size));
        return ResponseEntity.ok(pageOfProjects);
    }

    @Override
    public ResponseEntity<CategoryDto> updateCategory(UUID categoryId, CategoryUpdateDto categoryUpdateDto) {
        CategoryDto categoryDto = categoryService.updateCategory(categoryId, categoryUpdateDto);
        return ResponseEntity.ok(categoryDto);
    }

    @Override
    public ResponseEntity<ProjectDto> updateProjectForCategory(UUID categoryId, UUID projectId, ProjectUpdateDto projectUpdateDto) {
        ProjectDto projectDto = projectService.updateForCategory(categoryId, projectId, projectUpdateDto);
        return ResponseEntity.ok(projectDto);
    }
}
