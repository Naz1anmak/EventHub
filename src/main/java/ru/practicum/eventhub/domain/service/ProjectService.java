package ru.practicum.eventhub.domain.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.eventhub.api.dto.request.ProjectUpdateDto;
import ru.practicum.eventhub.api.dto.response.ProjectDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;

import java.util.UUID;

public interface ProjectService {
    PagedResponse<ProjectDto> getProjectsByCategory(UUID categoryId, Pageable pageable);

    ProjectDto getProjectByCategory(UUID categoryId, UUID projectId);

    ProjectDto updateForCategory(UUID categoryId, UUID projectId, ProjectUpdateDto dto);

    void deleteForCategory(UUID categoryId, UUID projectId);
}
