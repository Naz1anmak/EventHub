package ru.practicum.eventhub.domain.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.request.ProjectUpdateDto;
import ru.practicum.eventhub.api.exception.ConflictException;
import ru.practicum.eventhub.domain.model.Project;
import ru.practicum.eventhub.domain.service.impl.ProjectReadService;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectValidationService {
    private final ProjectReadService projectReadService;

    @Transactional(propagation = REQUIRES_NEW, readOnly = true)
    public void validateUpdate(ProjectUpdateDto dto, Project project) {
        validateName(dto.name(), project.getName());
    }

    @Transactional(readOnly = true)
    public void validateName(String newName, String currentName) {
        if (newName == null) return;
        if (newName.equals(currentName)) {
            log.error("Новое имя проекта совпадает с текущим");
            throw new ConflictException("Новое имя проекта совпадает с текущим");
        }
        projectReadService.checkExistsByName(newName);
    }
}
