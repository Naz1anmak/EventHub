package ru.practicum.eventhub.domain.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.api.dto.request.EventCreateDto;
import ru.practicum.eventhub.api.dto.request.EventUpdateDto;
import ru.practicum.eventhub.api.dto.request.TagCreateDto;
import ru.practicum.eventhub.domain.service.impl.TagReadService;

import java.util.Set;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventValidationService {
    private final TagReadService tagReadService;

    @Transactional(propagation = REQUIRES_NEW, readOnly = true)
    public void validateCreate(EventCreateDto dto) {
        validateTagName(dto.tags());
    }

    @Transactional(propagation = REQUIRES_NEW, readOnly = true)
    public void validateUpdate(EventUpdateDto dto) {
        validateTagName(dto.tags());
    }

    @Transactional(readOnly = true)
    public void validateTagName(Set<TagCreateDto> tags) {
        tags.forEach(tagDto ->
                tagReadService.checkExistsByName(tagDto.name())
        );
    }
}
