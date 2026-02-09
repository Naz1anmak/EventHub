package ru.practicum.eventhub.infrastructure;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.practicum.eventhub.api.dto.response.TagStatsDto;

import java.util.UUID;

@FeignClient(name = "tag-analytics", url = "${services.tag-analytics.url}")
public interface TagAnalyticsClient {

    @PostMapping("/api/v1/tags/{id}/used")
    void incrementUsage(@PathVariable UUID id);

    @GetMapping("/api/v1/tags/{id}/stats")
    TagStatsDto getStats(@PathVariable UUID id);
}
