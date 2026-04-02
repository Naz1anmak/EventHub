package ru.practicum.eventhub.config.feign;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.eventhub.api.dto.response.TagStatsDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TagAnalyticsFacade {
    private final TagAnalyticsClient tagAnalyticsClient;

    @Retry(name = "tag-analytics")
    public TagStatsDto createTagAnalytics(UUID tagId) {
        return tagAnalyticsClient.createTagAnalytics(tagId);
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
    public void deleteTagAnalytics(UUID tagId) {
        tagAnalyticsClient.deleteTagAnalytics(tagId);
    }
}
