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
import ru.practicum.eventhub.api.exception.types.NotFoundException;
import ru.practicum.eventhub.api.mapper.ProjectMapper;
import ru.practicum.eventhub.domain.dto.PagedResponse;
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
    public ProjectDto createForCategory(UUID categoryId, ProjectCreateDto dto) {
        Category category = categoryService.getCategoryByIdOrThrow(categoryId);
        User owner = userService.getUserByIdOrThrow(dto.ownerId());

        Project project = projectMapper.fromCreateDto(dto, category, owner);
        project = projectRepository.save(project);

        log.info("Создан проект с id={}", project.getId());
        return projectMapper.toDto(project);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProjectDto> getProjectsByCategory(UUID categoryId, Pageable pageable) {
        Page<Project> projectPage = projectRepository.findAllByCategoryId(categoryId, pageable);

        log.info("Получены проекты: страница {}, размер {}", pageable.getPageNumber(), pageable.getPageSize());
        return PagedResponse.from(projectPage, projectMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDto getProjectByCategory(UUID categoryId, UUID projectId) {
        Project project = getProjectByIdAndCategoryIdOrThrow(projectId, categoryId);
        log.info("Запрошен проект с id={} в категории с id={}", projectId, categoryId);
        return projectMapper.toDto(project);
    }

    @Override
    @Transactional
    public ProjectDto updateForCategory(UUID categoryId, UUID projectId, ProjectUpdateDto dto) {
        Project project = getProjectByIdAndCategoryIdOrThrow(projectId, categoryId);

        User owner = null;
        if (dto.ownerId() != null) {
            owner = userService.getUserByIdOrThrow(dto.ownerId());
        }

        Category category = null;
        if (dto.categoryId() != null) {
            category = categoryService.getCategoryByIdOrThrow(dto.categoryId());
        }

        projectMapper.updateProjectFromDto(dto, project, category, owner);
        project = projectRepository.save(project);

        log.info("Обновлен проект с id={} в категории с id={}", projectId, categoryId);
        return projectMapper.toDto(project);
    }

    @Override
    @Transactional
    public void deleteForCategory(UUID categoryId, UUID projectId) {
        getProjectByIdAndCategoryIdOrThrow(projectId, categoryId);
        projectRepository.deleteById(projectId);
        log.info("Удален проект с id={} в категории с id={}", projectId, categoryId);
    }

    public Project getProjectByIdAndCategoryIdOrThrow(UUID categoryId, UUID projectId) {
        return projectRepository.findByIdAndCategoryId(projectId, categoryId)
                .orElseThrow(() -> new NotFoundException("Проект с id=" + projectId + " в категории id=" + categoryId + " не найден"));
    }
}
