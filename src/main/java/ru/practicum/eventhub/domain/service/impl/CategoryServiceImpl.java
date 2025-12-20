package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.request.CategoryCreateDto;
import ru.practicum.eventhub.api.dto.request.CategoryUpdateDto;
import ru.practicum.eventhub.api.dto.request.ProjectCreateDto;
import ru.practicum.eventhub.api.dto.response.CategoryDto;
import ru.practicum.eventhub.api.exception.ConflictException;
import ru.practicum.eventhub.api.mapper.CategoryMapper;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.model.Category;
import ru.practicum.eventhub.domain.model.Project;
import ru.practicum.eventhub.domain.model.User;
import ru.practicum.eventhub.domain.repository.CategoryRepository;
import ru.practicum.eventhub.domain.service.CategoryService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryReader categoryReader;
    private final UserReader userReader;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryCreateDto dto) {
        Category category = categoryMapper.fromCreateDto(dto);

        Map<UUID, User> owners = getUserMap(dto.projects());

        for (ProjectCreateDto pDto : dto.projects()) {
            User owner = owners.get(pDto.ownerId());
            Project project = Project.create(pDto.name(), pDto.description(), owner);
            category.addProject(project);
        }

        try {
            category = categoryRepository.save(category);
        } catch (DataIntegrityViolationException exception) {
            log.error(exception.getMessage(), exception);
            throw new ConflictException("Категория с именем '" + dto.name() + "' уже существует.");
        }

        log.info("Создана категория с id={}", category.getId());
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<CategoryDto> getCategories(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        log.info("Отправлена страница категорий: страница={}, размер={}",
                pageable.getPageNumber(), pageable.getPageSize());
        return PagedResponse.from(categoryPage, categoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(UUID id) {
        Category category = categoryReader.findById(id);
        log.info("Отправлена категория c id={}", id);
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(UUID id, CategoryUpdateDto dto) {
        Category category = categoryReader.findById(id);

        Map<UUID, User> owners = getUserMap(dto.projects());

        Set<Project> projects = dto.projects().stream()
                .map(pDto -> {
                    User owner = owners.get(pDto.ownerId());
                    return Project.create(pDto.name(), pDto.description(), owner);
                })
                .collect(Collectors.toSet());

        dto.updateMode().apply(category, projects);

        categoryMapper.updateCategoryFromDto(dto, category);

        try {
            category = categoryRepository.save(category);
        } catch (DataIntegrityViolationException exception) {
            log.error(exception.getMessage(), exception);
            throw new ConflictException("Категория с именем '" + dto.name() + "' уже существует.");
        }

        log.info("Обновлена категория c id={}", id);
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        categoryReader.findById(id);
        categoryRepository.deleteById(id);
        log.info("Удалена категория с id={}", id);
    }

    @Transactional(readOnly = true)
    public Map<UUID, User> getUserMap(Set<ProjectCreateDto> dto) {
        return userReader.getUsersByIds(dto.stream()
                .map(ProjectCreateDto::ownerId)
                .collect(Collectors.toSet())
        );
    }
}
