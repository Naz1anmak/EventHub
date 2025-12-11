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
import ru.practicum.eventhub.api.dto.response.CategoryDto;
import ru.practicum.eventhub.api.exception.types.ConflictException;
import ru.practicum.eventhub.api.exception.types.NotFoundException;
import ru.practicum.eventhub.api.mapper.CategoryMapper;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.model.Category;
import ru.practicum.eventhub.domain.repository.CategoryRepository;
import ru.practicum.eventhub.domain.service.CategoryService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryCreateDto dto) {
        Category category = categoryMapper.fromCreateDto(dto);

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
        Category category = getCategoryByIdOrThrow(id);
        log.info("Отправлена категория c id={}", id);
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(UUID id, CategoryUpdateDto dto) {
        Category category = getCategoryByIdOrThrow(id);
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
        getCategoryByIdOrThrow(id);
        categoryRepository.deleteById(id);
        log.info("Удалена категория с id={}", id);
    }

    @Override
    public Category getCategoryByIdOrThrow(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с id=" + id + " не найдена."));
    }
}
