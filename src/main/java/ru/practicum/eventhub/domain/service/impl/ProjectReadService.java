package ru.practicum.eventhub.domain.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.exception.ConflictException;
import ru.practicum.eventhub.domain.model.Project;
import ru.practicum.eventhub.domain.repository.ProjectRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectReadService {
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public Project findByIdAndCategoryId(UUID projectId, UUID categoryId) {
        return projectRepository.findByIdAndCategoryId(projectId, categoryId).orElseThrow(() -> {
            log.error("Проект с id={} в категории с id={} не найден", projectId, categoryId);
            return new EntityNotFoundException("Проект с id=" + projectId + " в категории с id=" + categoryId + " не найден");
        });
    }

    @Transactional(readOnly = true)
    public void checkExistsByName(String name) {
        if (projectRepository.existsByName(name)) {
            log.error("Проект с именем='{}' уже существует", name);
            throw new ConflictException("Проект с именем='" + name + "' уже существует");
        }
    }
}
