package ru.practicum.eventhub.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.exception.NotFoundException;
import ru.practicum.eventhub.domain.model.Project;
import ru.practicum.eventhub.domain.repository.ProjectRepository;

import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectReader {
    private final ProjectRepository projectRepository;

    public Project findByIdAndCategoryId(UUID projectId, UUID categoryId) {
        return projectRepository.findByIdAndCategoryId(projectId, categoryId).orElseThrow(() -> {
            log.warn("Проект с id={} в категории с id={} не найден", projectId, categoryId);
            return new NotFoundException("Проект с id=" + projectId + " в категории с id=" + categoryId + " не найден");
        });
    }
}
