package ru.practicum.eventhub.domain.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.exception.ConflictException;
import ru.practicum.eventhub.domain.model.Category;
import ru.practicum.eventhub.domain.repository.CategoryRepository;

import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryReadService {
    private final CategoryRepository categoryRepository;

    public Category findById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(() -> {
            log.error("Категория с id={} не найдена", id);
            return new EntityNotFoundException("Категория с id=" + id + " не найдена");
        });
    }

    public void checkExistsByName(String name) {
        if (categoryRepository.existsByName(name)) {
            log.error("Категория с именем='{}' уже существует", name);
            throw new ConflictException("Категория с именем='" + name + "' уже существует");
        }
    }
}
