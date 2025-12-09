package ru.practicum.eventhub.api.controller;

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
import ru.practicum.eventhub.api.dto.request.ProjectCreateDto;
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
    public PagedResponse<CategoryDto> getCategories(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        return categoryService.getCategories(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable UUID id) {
        return categoryService.getCategoryById(id);
    }

    @PatchMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable UUID id, @Valid @RequestBody CategoryUpdateDto dto) {
        return categoryService.updateCategory(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
    }

    @PostMapping("/projects")
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDto createProject(@Valid @RequestBody ProjectCreateDto dto) {
        return projectService.createProject(dto);
    }

    @GetMapping("/projects")
    public PagedResponse<ProjectDto> getProjects(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        return projectService.getProjects(PageRequest.of(page, size));
    }

    @GetMapping("/projects/{id}")
    public ProjectDto getProjectById(@PathVariable UUID id) {
        return projectService.getProjectById(id);
    }

    @PatchMapping("/projects/{id}")
    public ProjectDto updateProject(@PathVariable UUID id, @Valid @RequestBody ProjectUpdateDto dto) {
        return projectService.updateProject(id, dto);
    }

    @DeleteMapping("/projects/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
    }
}
