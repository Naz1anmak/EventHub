package ru.practicum.eventhub.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventhub.api.dto.request.CategoryCreateDto;
import ru.practicum.eventhub.api.dto.request.CategoryUpdateDto;
import ru.practicum.eventhub.api.dto.response.CategoryDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.service.CategoryService;

import java.util.UUID;

@RestController
@RequestMapping("/categories")
@Validated
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryCreateDto dto) {
        return categoryService.createCategory(dto);
    }

    @GetMapping
    public PagedResponse<CategoryDto> getCategories(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        return categoryService.getCategories(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable UUID id) {
        return categoryService.getCategoryById(id);
    }

    @PatchMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable UUID id, @Valid @RequestBody CategoryUpdateDto dto) {
        return categoryService.updateCategory(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
    }
}
