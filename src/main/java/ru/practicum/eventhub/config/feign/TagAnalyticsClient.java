package ru.practicum.eventhub.config.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventhub.api.dto.response.TagStatsDto;
import ru.practicum.eventhub.config.feign.config.FeignConfig;
import ru.practicum.eventhub.config.feign.fallback.TagAnalyticsFallbackFactory;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@FeignClient(
        name = "tag-analytics",
        url = "${services.tag-analytics.url}",
        fallbackFactory = TagAnalyticsFallbackFactory.class,
        configuration = FeignConfig.class
)
public interface TagAnalyticsClient {

    @PostMapping("api/v1/tags/{id}")
    TagStatsDto createTagAnalytics(@PathVariable UUID id);

    @GetMapping("/api/v1/tags/{id}/stats")
    TagStatsDto getTagStats(@PathVariable UUID id);

    @PostMapping("/api/v1/tags/stats")
    Map<UUID, TagStatsDto> getTagStatsBatch(@RequestBody Set<UUID> tagIds);

    @DeleteMapping("/api/v1/tags/{id}")
    void deleteTagAnalytics(@PathVariable UUID id);
}
