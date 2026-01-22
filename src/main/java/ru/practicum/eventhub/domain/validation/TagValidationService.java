package ru.practicum.eventhub.domain.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.request.TagUpdateDto;
import ru.practicum.eventhub.api.exception.ConflictException;
import ru.practicum.eventhub.domain.model.Tag;
import ru.practicum.eventhub.domain.service.impl.TagReadService;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagValidationService {
    private final TagReadService tagReadService;

    @Transactional(propagation = REQUIRES_NEW, readOnly = true)
    public void validateUpdate(TagUpdateDto dto, Tag tag) {
        validateName(dto.name(), tag.getName());
    }

    private void validateName(String newName, String currentName) {
        if (newName == null) return;
        if (newName.equals(currentName)) {
            log.error("Новое имя тега совпадает с текущим");
            throw new ConflictException("Новое имя тега совпадает с текущим");
        }
        tagReadService.checkExistsByName(newName);
    }
}
