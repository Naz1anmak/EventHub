package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.request.ProjectCreateDto;
import ru.practicum.eventhub.api.dto.request.ProjectUpdateDto;
import ru.practicum.eventhub.api.dto.response.ProjectDto;
import ru.practicum.eventhub.api.mapper.ProjectMapper;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.exception.NotFoundException;
import ru.practicum.eventhub.domain.model.Category;
import ru.practicum.eventhub.domain.model.Project;
import ru.practicum.eventhub.domain.model.User;
import ru.practicum.eventhub.domain.repository.ProjectRepository;
import ru.practicum.eventhub.domain.service.CategoryService;
import ru.practicum.eventhub.domain.service.ProjectService;
import ru.practicum.eventhub.domain.service.UserService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final CategoryService categoryService;
    private final UserService userService;

    @Override
    @Transactional
    public ProjectDto createProject(ProjectCreateDto dto) {
        Category category = null;
        User owner;

        if (dto.categoryId() != null) {
            category = categoryService.getCategoryByIdOrThrow(dto.categoryId());
        }
        owner = userService.getUserByIdOrThrow(dto.ownerId());

        Project project = projectMapper.fromCreateDto(dto, category, owner);
        project = projectRepository.save(project);

        log.info("Создан проект: {}", project);
        return projectMapper.toDto(project);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProjectDto> getProjects(Pageable pageable) {
        Page<Project> projectPage = projectRepository.findAll(pageable);

        log.info("Получены проекты: страница {}, размер {}", pageable.getPageNumber(), pageable.getPageSize());
        return new PagedResponse<>(
                projectPage.getContent().stream().map(projectMapper::toDto).toList(),
                projectPage.getNumber(),
                projectPage.getSize(),
                projectPage.getTotalElements(),
                projectPage.getTotalPages()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDto getProjectById(UUID id) {
        Project project = getProjectByIdOrThrow(id);
        log.info("Запрошен проект с id={}", id);
        return projectMapper.toDto(project);
    }

    @Override
    @Transactional
    public ProjectDto updateProject(UUID id, ProjectUpdateDto dto) {
        Project project = getProjectByIdOrThrow(id);
        User owner = userService.getUserByIdOrThrow(dto.ownerId());

        Category category = null;
        if (dto.categoryId() != null) {
            category = categoryService.getCategoryByIdOrThrow(dto.categoryId());
        }

        projectMapper.updateProjectFromDto(dto, project, category, owner);
        project = projectRepository.save(project);

        log.info("Обновлен проект с id={}", id);
        return projectMapper.toDto(project);
    }

    @Override
    @Transactional
    public void deleteProject(UUID id) {
        getProjectByIdOrThrow(id);
        projectRepository.deleteById(id);
        log.info("Удален проект с id={}", id);
    }

    private Project getProjectByIdOrThrow(UUID id) {
        return projectRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Проект с id=" + id + " не найден"));
    }
}
