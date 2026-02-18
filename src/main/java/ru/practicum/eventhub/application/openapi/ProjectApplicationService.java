package ru.practicum.eventhub.application.openapi;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.eventhub.api.dto.response.ProjectDto;
import ru.practicum.eventhub.api.model.PageOfProjects;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.service.ProjectService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectApplicationService {
    private final ProjectService projectService;

    public PageOfProjects getProjectsByCategory(UUID categoryId, Pageable pageable) {
        PagedResponse<ProjectDto> page = projectService.getProjectsByCategory(categoryId, pageable);
        return getPageOfProjects(page);
    }

    public static @NotNull PageOfProjects getPageOfProjects(PagedResponse<ProjectDto> page) {
        PageOfProjects apiPage = new PageOfProjects();
        apiPage.setPage(page.page());
        apiPage.setSize(page.size());
        apiPage.setTotalElements(page.totalElements());
        apiPage.setTotalPages(page.totalPages());
        apiPage.setContent(page.content());
        return apiPage;
    }
}
