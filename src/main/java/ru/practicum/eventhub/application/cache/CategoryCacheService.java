package ru.practicum.eventhub.application.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;
import ru.practicum.eventhub.api.dto.response.CategoryDto;
import ru.practicum.eventhub.api.mapper.CategoryMapper;
import ru.practicum.eventhub.domain.model.Category;
import ru.practicum.eventhub.domain.service.impl.CategoryReadService;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryCacheService {
    private final CategoryReadService categoryReadService;
    private final CategoryMapper categoryMapper;

    @CachePut(value = "categories", key = "#categoryId")
    public CategoryDto refresh(UUID categoryId) {
        Category category = categoryReadService.findById(categoryId);
        log.info("Обновлен кеш категории c id={}", categoryId);
        return categoryMapper.toDto(category);
    }
}
