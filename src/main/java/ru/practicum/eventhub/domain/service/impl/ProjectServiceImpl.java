package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.request.ProjectUpdateDto;
import ru.practicum.eventhub.api.dto.response.ProjectDto;
import ru.practicum.eventhub.api.mapper.ProjectMapper;
import ru.practicum.eventhub.application.cache.CategoryCacheService;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.model.Category;
import ru.practicum.eventhub.domain.model.Project;
import ru.practicum.eventhub.domain.repository.ProjectRepository;
import ru.practicum.eventhub.domain.service.ProjectService;
import ru.practicum.eventhub.domain.util.PageValidator;
import ru.practicum.eventhub.domain.validation.ProjectValidationService;
import ru.practicum.eventhub.infrastructure.redis.ProjectCacheIndexService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectReadService projectReadService;
    private final CategoryReadService categoryReadService;
    private final ProjectValidationService projectValidationService;
    private final ProjectCacheIndexService cacheIndexService;
    private final CategoryCacheService categoryCacheService;

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProjectDto> getProjectsByCategory(UUID categoryId, Pageable pageable) {
        categoryReadService.findById(categoryId);
        Page<Project> page = projectRepository.findAllByCategoryId(categoryId, pageable);

        PageValidator.validatePage(page);

        log.info("Получены проекты: страница {}, размер {}", pageable.getPageNumber(), pageable.getPageSize());
        return PagedResponse.from(page, projectMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "projects", key = "#categoryId + ':' + #projectId")
    public ProjectDto getProjectByCategory(UUID categoryId, UUID projectId) {
        categoryReadService.findById(categoryId);
        Project project = projectReadService.findByIdAndCategoryId(projectId, categoryId);

        cacheIndexService.addProject(categoryId, projectId);

        log.info("Запрошен проект с id={} в категории с id={}", projectId, categoryId);
        return projectMapper.toDto(project);
    }

    @Override
    @Transactional
    @CachePut(value = "projects", key = "#categoryId + ':' + #projectId")
    public ProjectDto updateForCategory(UUID categoryId, UUID projectId, ProjectUpdateDto dto) {
        categoryReadService.findById(categoryId);
        Project project = projectReadService.findByIdAndCategoryId(projectId, categoryId);
        projectValidationService.validateUpdate(dto, project);

        project = projectMapper.updateProjectFromDto(dto, project);
        project = projectRepository.save(project);

        log.info("Обновлен проект с id={}", projectId);
        return projectMapper.toDto(project);
    }

    @Override
    @Transactional
    @CacheEvict(value = "projects", key = "#categoryId + ':' + #projectId")
    public void deleteForCategory(UUID categoryId, UUID projectId) {
        Category category = categoryReadService.findById(categoryId);
        Project project = projectReadService.findByIdAndCategoryId(projectId, categoryId);

        category.removeProject(project);

        cacheIndexService.removeProject(categoryId, projectId);
        categoryCacheService.refresh(categoryId);

        log.info("Удален проект с id={} в категории с id={}", projectId, categoryId);
    }
}
