package ru.practicum.eventhub.infrastructure.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;
import ru.practicum.eventhub.api.dto.response.EventDto;
import ru.practicum.eventhub.api.mapper.EventMapper;
import ru.practicum.eventhub.domain.model.Event;
import ru.practicum.eventhub.domain.service.impl.EventReadService;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventCacheService {
    private final EventReadService eventReadService;
    private final EventMapper eventMapper;
    private final CacheManager cacheManager;

    @CachePut(value = "events", key = "#eventId")
    public EventDto refresh(UUID eventId) {
        Event event = eventReadService.findById(eventId);
        log.info("Обновлен кэш события с id={}", eventId);
        return eventMapper.toDto(event);
    }

    public void evict(UUID eventId) {
        Cache cache = cacheManager.getCache("events");
        if (cache != null) {
            cache.evict(eventId);
            log.info("Удален кэш события с id={}", eventId);
        }
    }
}

