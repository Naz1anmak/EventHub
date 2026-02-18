package ru.practicum.eventhub.application.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;
import ru.practicum.eventhub.api.dto.response.TagDto;
import ru.practicum.eventhub.api.dto.response.TagStatsDto;
import ru.practicum.eventhub.api.dto.response.TagWithStatsDto;
import ru.practicum.eventhub.api.mapper.TagMapper;
import ru.practicum.eventhub.domain.model.Tag;
import ru.practicum.eventhub.domain.service.impl.TagReadService;
import ru.practicum.eventhub.infrastructure.feign.TagAnalyticsClient;
import ru.practicum.eventhub.infrastructure.redis.ManyToManyCacheIndexService;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TagCacheService {
    private static final String EVENT_TAG_RELATION = "event_tag";

    private final TagReadService tagReadService;
    private final TagMapper tagMapper;
    private final ManyToManyCacheIndexService relationIndexService;
    private final CacheManager cacheManager;
    private final TagAnalyticsClient tagAnalyticsClient;

    @CachePut(value = "tags", key = "#tagId")
    public TagDto refresh(UUID tagId) {
        Tag tag = tagReadService.findById(tagId);
        log.info("Обновлен кеш тега с id={}", tagId);
        return tagMapper.toDto(tag);
    }

    public void refreshByEvent(UUID eventId, UUID tagId) {
        Cache cache = cacheManager.getCache("tags_by_event");
        if (cache == null) return;

        Tag tag = tagReadService.findByIdAndEventsId(tagId, eventId);
        TagStatsDto stats = tagAnalyticsClient.getStats(tagId);

        TagWithStatsDto dto = tagMapper.toDtoWithStats(tag, stats);

        cache.put(eventId + ":" + tagId, dto);

        log.info("Обновлен кэш тега с id={} для события с id={}", tagId, eventId);
    }

    public void refreshCompositeCacheForTag(UUID tagId, Set<UUID> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) return;

        Cache cache = cacheManager.getCache("tags_by_event");
        if (cache == null) return;

        Tag tag = tagReadService.findById(tagId);
        TagStatsDto stats = tagAnalyticsClient.getStats(tagId);

        TagWithStatsDto dto = tagMapper.toDtoWithStats(tag, stats);

        for (UUID eventId : eventIds) {
            cache.put(eventId + ":" + tagId, dto);

            log.info("Обновлен кэш тега с id={} для события с id={}", tagId, eventId);
        }
    }

    public void evictAll(UUID tagId) {
        Cache mainCache = cacheManager.getCache("tags");
        if (mainCache != null) {
            mainCache.evict(tagId);
        }

        Set<UUID> eventIds = relationIndexService.getLeftIds(EVENT_TAG_RELATION, tagId);

        Cache eventCache = cacheManager.getCache("tags_by_event");
        if (eventCache != null) {
            for (UUID eventId : eventIds) {
                eventCache.evict(eventId + ":" + tagId);
                log.info("Удален кэш тега с id={} для события с id={}", tagId, eventId);
            }
        }

        relationIndexService.deleteRight(EVENT_TAG_RELATION, tagId);

        log.info("Полностью очищен кэш и индекс для тега id={}", tagId);
    }
}
