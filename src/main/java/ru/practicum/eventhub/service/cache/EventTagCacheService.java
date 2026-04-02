package ru.practicum.eventhub.service.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import ru.practicum.eventhub.api.dto.response.TagStatsDto;
import ru.practicum.eventhub.api.dto.response.TagWithStatsDto;
import ru.practicum.eventhub.api.mapper.TagMapper;
import ru.practicum.eventhub.config.feign.TagAnalyticsFacade;
import ru.practicum.eventhub.config.redis.ManyToManyCacheIndexService;
import ru.practicum.eventhub.config.redis.RedisCacheService;
import ru.practicum.eventhub.model.Tag;
import ru.practicum.eventhub.service.impl.TagReadService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventTagCacheService {
    private static final String EVENT_TAG_RELATION = "event_tag";

    private final TagReadService tagReadService;
    private final TagMapper tagMapper;
    private final ManyToManyCacheIndexService relationIndexService;
    private final CacheManager cacheManager;
    private final TagAnalyticsFacade tagAnalyticsFacade;
    private final RedisCacheService cacheService;

    public void refreshByEventBatch(UUID eventId, Set<UUID> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) return;

        Cache cache = cacheManager.getCache("tags_by_event");
        if (cache == null) return;

        List<Tag> tags = tagReadService.findAllByIdInAndEventsId(tagIds, eventId);

        Map<UUID, TagStatsDto> statsMap = tagAnalyticsFacade.getTagStatsBatch(tagIds);

        for (Tag tag : tags) {
            TagStatsDto stats = statsMap.get(tag.getId());
            TagWithStatsDto dto = tagMapper.toDtoWithStats(tag, stats);
            cache.put(eventId + ":" + tag.getId(), dto);
            log.debug("Обновлен кеш ивента с id={} для тега с id={}", eventId, tag.getId());
        }

        log.info("Обновлен кеш ивента с id={} для всех связанных тегов", eventId);
    }

    public void refreshCompositeCacheForTag(Tag tag, Set<UUID> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) return;

        Cache cache = cacheManager.getCache("tags_by_event");
        if (cache == null) return;

        UUID tagId = tag.getId();
        TagStatsDto stats = tagAnalyticsFacade.getTagStats(tagId);

        TagWithStatsDto dto = tagMapper.toDtoWithStats(tag, stats);

        for (UUID eventId : eventIds) {
            cache.put(eventId + ":" + tagId, dto);
            log.debug("Обновлен кэш тега с id={} для события с id={}", tagId, eventId);
        }

        log.info("Обновлен кэш тега с id={} для всех связанных событий", tagId);
    }

    public void evictEventTagCache(UUID tagId) {
        Set<UUID> eventIds = relationIndexService.getLeftIds(EVENT_TAG_RELATION, tagId);

        List<String> keys = eventIds.stream()
                .map(eventId -> eventId + ":" + tagId)
                .toList();

        cacheService.evictAll("tags_by_event", keys);

        relationIndexService.deleteRight(EVENT_TAG_RELATION, tagId);

        log.info("Полностью очищен кэш и индекс для тега id={}", tagId);
    }
}
