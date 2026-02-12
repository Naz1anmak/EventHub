package ru.practicum.eventhub.infrastructure.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.eventhub.api.dto.response.TagStatsDto;
import ru.practicum.eventhub.infrastructure.TagAnalyticsClient;

import java.util.UUID;

@Slf4j
@Component
public class TagAnalyticsFallback implements TagAnalyticsClient {

    @Override
    public void incrementUsage(UUID id) {
        log.warn("Сервис Tag Analytics недоступен. Не удалось увеличить счетчик использования для тега с id={}", id);
    }

    @Override
    public TagStatsDto getStats(UUID id) {
        log.warn("Сервис Tag Analytics недоступен. Не удалось получить статистику для тега с id={}", id);
        return new TagStatsDto(0L, null);
    }
}
