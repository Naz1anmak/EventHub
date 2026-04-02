package ru.practicum.eventhub.service.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.eventhub.config.redis.ProjectCacheIndexService;
import ru.practicum.eventhub.config.redis.RedisCacheService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryProjectCacheService {
    private final ProjectCacheIndexService cacheIndexService;
    private final RedisCacheService cacheService;

    public void evictProjectsByCategoryId(UUID categoryId) {
        Set<String> projectIds = cacheIndexService.getProjects(categoryId);

        List<String> keys = projectIds.stream()
                .map(projectId -> categoryId + ":" + projectId)
                .toList();

        cacheService.evictAll("projects", keys);

        cacheIndexService.deleteCategoryIndex(categoryId);
        log.info("Полностью очищен кэш и индекс для категории id={}", categoryId);
    }
}
