package ru.practicum.eventhub.application.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.practicum.eventhub.domain.event.CategoryDeletedEvent;
import ru.practicum.eventhub.infrastructure.redis.ProjectCacheIndexService;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryCacheInvalidator {
    private final CacheManager cacheManager;
    private final ProjectCacheIndexService cacheIndexService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(CategoryDeletedEvent event) {
        UUID categoryId = event.categoryId();

        Optional.ofNullable(cacheManager.getCache("projects"))
                .ifPresent(projectsCache -> {
                    Set<String> projectIds = cacheIndexService.getProjects(categoryId);
                    if (projectIds != null) {
                        projectIds.forEach(pid -> projectsCache.evict(categoryId + ":" + pid));
                    }
                });

        cacheIndexService.deleteCategoryIndex(categoryId);
    }
}
