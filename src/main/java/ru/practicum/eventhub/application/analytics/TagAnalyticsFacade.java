package ru.practicum.eventhub.application.analytics;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.eventhub.api.dto.response.TagStatsDto;
import ru.practicum.eventhub.infrastructure.feign.TagAnalyticsClient;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TagAnalyticsFacade {
    private final TagAnalyticsClient tagAnalyticsClient;

    @Retry(name = "tag-analytics")
    public TagStatsDto sendAnalytics(UUID tagId) {
        return tagAnalyticsClient.createIfAbsent(tagId);
    }

    @Retry(name = "tag-analytics")
    public TagStatsDto getTagStats(UUID tagId) {
        return tagAnalyticsClient.getTagStats(tagId);
    }

    @Retry(name = "tag-analytics")
    public Map<UUID, TagStatsDto> getTagStatsBatch(Set<UUID> tagIds) {
        return tagAnalyticsClient.getTagStatsBatch(tagIds);
    }

    @Retry(name = "tag-analytics")
    public void delete(UUID tagId) {
        tagAnalyticsClient.deleteTagAnalytics(tagId);
    }
}
