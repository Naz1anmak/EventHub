package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.exception.NotFoundException;
import ru.practicum.eventhub.domain.model.Category;
import ru.practicum.eventhub.domain.repository.CategoryRepository;

import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryReader {
    private final CategoryRepository categoryRepository;

    public Category findById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(() -> {
            log.warn("Категория с id={} не найдена.", id);
            return new NotFoundException("Категория с id=" + id + " не найдена.");
        });
    }
}
