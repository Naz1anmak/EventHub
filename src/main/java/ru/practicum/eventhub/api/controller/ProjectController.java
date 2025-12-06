package ru.practicum.eventhub.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventhub.api.dto.request.ProjectCreateDto;
import ru.practicum.eventhub.api.dto.request.ProjectUpdateDto;
import ru.practicum.eventhub.api.dto.response.ProjectDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.service.ProjectService;

import java.util.UUID;

@RestController
@RequestMapping("/projects")
@Validated
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDto createProject(@Valid @RequestBody ProjectCreateDto dto) {
        return projectService.createProject(dto);
    }

    @GetMapping
    public PagedResponse<ProjectDto> getProjects(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        return projectService.getProjects(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ProjectDto getProjectById(@PathVariable UUID id) {
        return projectService.getProjectById(id);
    }

    @PatchMapping("/{id}")
    public ProjectDto updateProject(@PathVariable UUID id, @Valid @RequestBody ProjectUpdateDto dto) {
        return projectService.updateProject(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
    }
}
