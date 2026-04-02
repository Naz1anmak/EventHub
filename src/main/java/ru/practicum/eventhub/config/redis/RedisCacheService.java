package ru.practicum.eventhub.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class RedisCacheService {
    private final CacheManager cacheManager;

    public void evictAll(String cacheName, Collection<?> keys) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            for (Object key : keys) {
                cache.evict(key);
            }
        }
    }
}
