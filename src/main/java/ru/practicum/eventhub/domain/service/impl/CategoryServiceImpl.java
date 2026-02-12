package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.request.CategoryCreateDto;
import ru.practicum.eventhub.api.dto.request.CategoryUpdateDto;
import ru.practicum.eventhub.api.dto.response.CategoryDto;
import ru.practicum.eventhub.api.mapper.CategoryMapper;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.event.CategoryDeletedEvent;
import ru.practicum.eventhub.domain.model.Category;
import ru.practicum.eventhub.domain.repository.CategoryRepository;
import ru.practicum.eventhub.domain.service.CategoryService;
import ru.practicum.eventhub.domain.util.PageValidator;
import ru.practicum.eventhub.domain.validation.CategoryValidationService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryReadService categoryReadService;
    private final CategoryValidationService categoryValidationService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryCreateDto dto) {
        categoryValidationService.validateCreate(dto);

        Category category = categoryMapper.fromCreateDto(dto);

        category = categoryRepository.save(category);
        log.info("Создана категория с id={}", category.getId());
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<CategoryDto> getCategories(Pageable pageable) {
        Page<Category> page = categoryRepository.findAll(pageable);
        PageValidator.validatePage(page);

        log.info("Отправлена страница категорий: страница={}, размер={}",
                pageable.getPageNumber(), pageable.getPageSize());
        return PagedResponse.from(page, categoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "categories", key = "#id")
    public CategoryDto getCategoryById(UUID id) {
        Category category = categoryReadService.findById(id);
        log.info("Отправлена категория c id={}", id);
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    @CachePut(value = "categories", key = "#id")
    public CategoryDto updateCategory(UUID id, CategoryUpdateDto dto) {
        Category category = categoryReadService.findByIdForUpdate(id);

        categoryValidationService.validateUpdate(dto, category);

        category = categoryMapper.updateCategoryFromDto(dto, category);

        category = categoryRepository.save(category);
        log.info("Обновлена категория c id={}", id);
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    @CacheEvict(value = "categories", key = "#id")
    public void deleteCategory(UUID id) {
        categoryReadService.findById(id);
        categoryRepository.deleteById(id);

        eventPublisher.publishEvent(new CategoryDeletedEvent(id));
        log.info("Удалена категория с id={}", id);
    }
}
