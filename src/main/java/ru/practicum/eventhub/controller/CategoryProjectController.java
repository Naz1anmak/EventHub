package ru.practicum.eventhub.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventhub.api.dto.request.CategoryCreateDto;
import ru.practicum.eventhub.api.dto.request.CategoryUpdateDto;
import ru.practicum.eventhub.api.dto.request.ProjectUpdateDto;
import ru.practicum.eventhub.api.dto.response.CategoryDto;
import ru.practicum.eventhub.api.dto.response.ProjectDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.service.CategoryService;
import ru.practicum.eventhub.domain.service.ProjectService;

import java.util.UUID;

@RestController
@RequestMapping("/categories/v1/api")
@Validated
@RequiredArgsConstructor
public class CategoryProjectController {
    private final CategoryService categoryService;
    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryCreateDto dto) {
        return categoryService.createCategory(dto);
    }

    @GetMapping
    public PagedResponse<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
                                                    @RequestParam(defaultValue = "10") @Positive Integer size) {
        return categoryService.getCategories(PageRequest.of(page, size));
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getCategoryById(@PathVariable UUID categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @PatchMapping("/{categoryId}")
    public CategoryDto updateCategory(@PathVariable UUID categoryId, @Valid @RequestBody CategoryUpdateDto dto) {
        return categoryService.updateCategory(categoryId, dto);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable UUID categoryId) {
        categoryService.deleteCategory(categoryId);
    }

    @GetMapping("/{categoryId}/projects")
    public PagedResponse<ProjectDto> getProjectsByCategory(@PathVariable UUID categoryId,
                                                           @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
                                                           @RequestParam(defaultValue = "10") @Positive Integer size) {
        return projectService.getProjectsByCategory(categoryId, PageRequest.of(page, size));
    }

    @GetMapping("/{categoryId}/projects/{projectId}")
    public ProjectDto getProjectByCategory(@PathVariable UUID categoryId, @PathVariable UUID projectId) {
        return projectService.getProjectByCategory(categoryId, projectId);
    }

    @PatchMapping("/{categoryId}/projects/{projectId}")
    public ProjectDto updateProjectForCategory(@PathVariable UUID categoryId,
                                               @PathVariable UUID projectId,
                                               @Valid @RequestBody ProjectUpdateDto dto) {
        return projectService.updateForCategory(categoryId, projectId, dto);
    }

    @DeleteMapping("/{categoryId}/projects/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectForCategory(@PathVariable UUID categoryId, @PathVariable UUID projectId) {
        projectService.deleteForCategory(categoryId, projectId);
    }
}
