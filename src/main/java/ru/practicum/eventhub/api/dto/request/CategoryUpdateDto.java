package ru.practicum.eventhub.api.dto.request;

import jakarta.validation.constraints.Size;
import ru.practicum.eventhub.domain.model.ProjectUpdateMode;

import java.util.Set;

public record CategoryUpdateDto(

        @Size(max = 100, message = "Иmя должно быть до 100 символов")
        String name,

        @Size(max = 255, message = "Описание должно быть до 255 символов")
        String description,

        Set<ProjectCreateDto> projects,
        ProjectUpdateMode updateMode
) {
    public CategoryUpdateDto {
        if (projects == null) {
            projects = Set.of();
        }

        if (updateMode == null) {
            updateMode = ProjectUpdateMode.ADD;
        }
    }
}
