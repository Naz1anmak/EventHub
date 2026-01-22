package ru.practicum.eventhub.domain.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.request.CategoryUpdateDto;
import ru.practicum.eventhub.api.dto.request.ProjectCreateDto;
import ru.practicum.eventhub.api.exception.ConflictException;
import ru.practicum.eventhub.domain.model.Category;
import ru.practicum.eventhub.domain.service.impl.CategoryReadService;
import ru.practicum.eventhub.domain.service.impl.ProjectReadService;

import java.util.Set;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryValidationService {
    private final CategoryReadService categoryReadService;
    private final ProjectReadService projectReadService;

    @Transactional(propagation = REQUIRES_NEW, readOnly = true)
    public void validateUpdate(CategoryUpdateDto dto, Category category) {
        validateName(dto.name(), category.getName());
        validateProjectName(dto.projects());
    }

    private void validateName(String newName, String currentName) {
        if (newName == null) return;
        if (newName.equals(currentName)) {
            log.error("Новое имя категории совпадает с текущим");
            throw new ConflictException("Новое имя категории совпадает с текущим");
        }
        categoryReadService.checkExistsByName(newName);
    }

    private void validateProjectName(Set<ProjectCreateDto> projects) {
        projects.forEach(projectDto ->
                projectReadService.checkExistsByName(projectDto.name())
        );
    }
}
