package ru.practicum.eventhub.infrastructure.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.eventhub.api.dto.response.TagStatsDto;
import ru.practicum.eventhub.infrastructure.feign.TagAnalyticsClient;

import java.util.UUID;

@Slf4j
@Component
public class TagAnalyticsFallback implements TagAnalyticsClient {

    @Override
    public void createIfAbsent(UUID id) {
        log.warn("Сервис Tag Analytics недоступен. Не удалось создать статистику для тега с id={}", id);
    }

    @Override
    public TagStatsDto getStats(UUID id) {
        log.warn("Сервис Tag Analytics недоступен. Не удалось получить статистику для тега с id={}", id);
        return new TagStatsDto(null);
    }
}
