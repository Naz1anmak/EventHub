package ru.practicum.eventhub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.eventhub.api.CategoriesApi;
import ru.practicum.eventhub.api.mapper.CategoryApiMapper;
import ru.practicum.eventhub.api.mapper.ProjectApiMapper;
import ru.practicum.eventhub.api.model.*;
import ru.practicum.eventhub.domain.service.CategoryService;
import ru.practicum.eventhub.domain.service.ProjectService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CategoryProjectController implements CategoriesApi {
    private final CategoryService categoryService;
    private final ProjectService projectService;
    private final CategoryApiMapper categoryApiMapper;
    private final ProjectApiMapper projectApiMapper;

    @Override
    public ResponseEntity<CategoryDto> createCategory(CategoryCreateDto categoryCreateDto) {
        var serviceCreateDto = categoryApiMapper.toServiceDto(categoryCreateDto);
        var serviceResult = categoryService.createCategory(serviceCreateDto);
        var apiResult = categoryApiMapper.toApiDto(serviceResult);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResult);
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
    public ResponseEntity<PagedCategoryDtoResponse> getCategories(Integer page, Integer size) {
        var servicePage = categoryService.getCategories(PageRequest.of(page, size));
        return ResponseEntity.ok(categoryApiMapper.toApiPage(servicePage));
    }

    @Override
    public ResponseEntity<CategoryDto> getCategoryById(UUID categoryId) {
        var servicePage = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(categoryApiMapper.toApiDto(servicePage));
    }

    @Override
    public ResponseEntity<ProjectDto> getProjectByCategoryId(UUID categoryId, UUID projectId) {
        var serviceDto = projectService.getProjectByCategory(categoryId, projectId);
        return ResponseEntity.ok(projectApiMapper.toApiDto(serviceDto));
    }

    @Override
    public ResponseEntity<PagedProjectDtoResponse> getProjectsByCategoryId(UUID categoryId, Integer page, Integer size) {
        var servicePage = projectService.getProjectsByCategory(categoryId, PageRequest.of(page, size));
        return ResponseEntity.ok(projectApiMapper.toApiPage(servicePage));
    }

    @Override
    public ResponseEntity<CategoryDto> updateCategory(UUID categoryId, CategoryUpdateDto categoryUpdateDto) {
        var serviceUpdateDto = categoryApiMapper.toServiceDto(categoryUpdateDto);
        var serviceResult = categoryService.updateCategory(categoryId, serviceUpdateDto);
        var apiResult = categoryApiMapper.toApiDto(serviceResult);

        return ResponseEntity.ok(apiResult);
    }

    @Override
    public ResponseEntity<ProjectDto> updateProjectForCategory(UUID categoryId, UUID projectId, ProjectUpdateDto projectUpdateDto) {
        var serviceUpdateDto = projectApiMapper.toServiceDto(projectUpdateDto);
        var serviceResult = projectService.updateForCategory(categoryId, projectId, serviceUpdateDto);
        var apiResult = projectApiMapper.toApiDto(serviceResult);

        return ResponseEntity.ok(apiResult);
    }
}
