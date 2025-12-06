package ru.practicum.eventhub.domain.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.eventhub.api.dto.request.CategoryCreateDto;
import ru.practicum.eventhub.api.dto.request.CategoryUpdateDto;
import ru.practicum.eventhub.api.dto.response.CategoryDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.model.Category;

import java.util.UUID;

public interface CategoryService {
    CategoryDto createCategory(CategoryCreateDto dto);

    PagedResponse<CategoryDto> getCategories(Pageable pageable);

    CategoryDto getCategoryById(UUID id);

    CategoryDto updateCategory(UUID id, CategoryUpdateDto dto);

    void deleteCategory(UUID id);

    Category getCategoryByIdOrThrow(UUID id);
}
