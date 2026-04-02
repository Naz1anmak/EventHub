package ru.practicum.eventhub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.request.CategoryCreateDto;
import ru.practicum.eventhub.api.dto.request.CategoryUpdateDto;
import ru.practicum.eventhub.api.dto.response.CategoryDto;
import ru.practicum.eventhub.api.mapper.CategoryMapper;
import ru.practicum.eventhub.api.model.PageOfCategories;
import ru.practicum.eventhub.model.Category;
import ru.practicum.eventhub.repository.CategoryRepository;
import ru.practicum.eventhub.service.CategoryService;
import ru.practicum.eventhub.service.cache.CategoryProjectCacheService;
import ru.practicum.eventhub.util.PageValidator;
import ru.practicum.eventhub.validation.CategoryValidationService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryReadService categoryReadService;
    private final CategoryValidationService categoryValidationService;
    private final CategoryProjectCacheService categoryProjectCacheService;

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
    public PageOfCategories getCategories(Pageable pageable) {
        Page<Category> page = categoryRepository.findAll(pageable);
        PageValidator.validatePage(page);

        log.info("Отправлена страница категорий: страница={}, размер={}",
                pageable.getPageNumber(), pageable.getPageSize());
        return toApiPage(page, categoryMapper);
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

        categoryProjectCacheService.evictProjectsByCategoryId(id);
        log.info("Удалена категория с id={}", id);
    }

    private PageOfCategories toApiPage(Page<Category> page, CategoryMapper mapper) {
        PageOfCategories apiPage = new PageOfCategories();
        apiPage.setContent(page.getContent().stream().map(mapper::toDto).toList());
        apiPage.setPageNumber(page.getNumber());
        apiPage.setPageSize(page.getSize());
        apiPage.setTotalElements(page.getTotalElements());
        apiPage.setTotalPages(page.getTotalPages());
        return apiPage;
    }
}
