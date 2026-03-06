package ru.practicum.eventhub.application.saga;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.eventhub.api.dto.response.TagStatsDto;
import ru.practicum.eventhub.api.dto.response.TagWithStatsDto;
import ru.practicum.eventhub.api.mapper.TagMapper;
import ru.practicum.eventhub.application.analytics.TagAnalyticsFacade;
import ru.practicum.eventhub.domain.model.Tag;
import ru.practicum.eventhub.domain.service.TagService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagAnalyticsSagaOrchestrator {
    private final TagService tagService;
    private final TagAnalyticsFacade analyticsFacade;
    private final TagMapper tagMapper;

    public TagWithStatsDto addTagToEvent(UUID eventId, UUID tagId) {
        boolean tagAdded = false;
        boolean analyticsCreated = false;

        try {
            Tag tag = tagService.addTagLocal(eventId, tagId);
            tagAdded = true;

            TagStatsDto tagStatsDto = analyticsFacade.sendAnalytics(tagId);
            analyticsCreated = true;

            tagService.cacheRefreshAfterAddTag(eventId, tagId);

            log.info("Тег с id={} добавлен к событию с id={}", tagId, eventId);
            return tagMapper.toDtoWithStats(tag, tagStatsDto);

        } catch (Exception ex) {
            log.error("Ошибка при добавлении тега с id={} к событию с id={}: {}", tagId, eventId, ex.getMessage());

            if (analyticsCreated) {
                analyticsFacade.delete(tagId);
            }

            if (tagAdded) {
                tagService.removeTagLocal(eventId, tagId);
            }

            throw ex;
        }
    }
}
