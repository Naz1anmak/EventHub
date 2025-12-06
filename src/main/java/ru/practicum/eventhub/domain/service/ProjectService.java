package ru.practicum.eventhub.domain.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.eventhub.api.dto.request.ProjectCreateDto;
import ru.practicum.eventhub.api.dto.request.ProjectUpdateDto;
import ru.practicum.eventhub.api.dto.response.ProjectDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;

import java.util.UUID;

public interface ProjectService {
    ProjectDto createProject(ProjectCreateDto dto);

    PagedResponse<ProjectDto> getProjects(Pageable pageable);

    ProjectDto getProjectById(UUID id);

    ProjectDto updateProject(UUID id, ProjectUpdateDto dto);

    void deleteProject(UUID id);
}
