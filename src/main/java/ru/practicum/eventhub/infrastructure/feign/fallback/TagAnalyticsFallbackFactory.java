package ru.practicum.eventhub.infrastructure.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.practicum.eventhub.api.dto.response.TagStatsDto;
import ru.practicum.eventhub.infrastructure.feign.TagAnalyticsClient;
import ru.practicum.eventhub.infrastructure.feign.exception.TagAnalyticsClientException;
import ru.practicum.eventhub.infrastructure.feign.exception.TagNotFoundException;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
public class TagAnalyticsFallbackFactory implements FallbackFactory<TagAnalyticsClient> {

    @Override
    public TagAnalyticsClient create(Throwable cause) {
        return new TagAnalyticsClientFallback(cause);
    }

    @Slf4j
    private static class TagAnalyticsClientFallback implements TagAnalyticsClient {
        private final Throwable cause;

        public TagAnalyticsClientFallback(Throwable cause) {
            this.cause = cause;
        }

        @Override
        public TagStatsDto incrementUsage(UUID id) {
            if (cause instanceof TagNotFoundException) {
                log.debug("Тег с id={} не найден в Tag Analytics (404). Пропускаем создание статистики.", id);
                throw (TagNotFoundException) cause;
            }

            if (cause instanceof TagAnalyticsClientException) {
                log.warn("Ошибка клиента при создании статистики для тега id={}: {}", id, cause.getMessage());
                throw (TagAnalyticsClientException) cause;
            }

            log.warn("Сервис Tag Analytics недоступен. Не удалось создать статистику для тега с id={}. " +
                    "Причина: {}", id, cause.getClass().getSimpleName(), cause);
            return new TagStatsDto(0, null, null);
        }

        @Override
        public TagStatsDto getTagStats(UUID id) {
            if (cause instanceof TagNotFoundException) {
                log.debug("Тег с id={} не найден в Tag Analytics (404). Возвращаем пустую статистику.", id);
                return new TagStatsDto(0, null, null);
            }

            if (cause instanceof TagAnalyticsClientException) {
                log.warn("Ошибка клиента при получении статистики для тега id={}: {}", id, cause.getMessage());
                throw (TagAnalyticsClientException) cause;
            }

            log.warn("Сервис Tag Analytics недоступен. Не удалось получить статистику для тега с id={}. " +
                    "Причина: {}", id, cause.getClass().getSimpleName(), cause);
            return new TagStatsDto(0, null, null);
        }

        @Override
        public Map<UUID, TagStatsDto> getTagStatsBatch(Set<UUID> tagIds) {
            if (cause instanceof TagNotFoundException) {
                log.debug("Некоторые теги с id={} не найдены в Tag Analytics (404).", tagIds);
                throw (TagNotFoundException) cause;
            }

            if (cause instanceof TagAnalyticsClientException) {
                log.warn("Ошибка клиента при получении статистики для тегов id={}: {}", tagIds, cause.getMessage());
                throw (TagAnalyticsClientException) cause;
            }

            log.warn("Сервис Tag Analytics недоступен. Не удалось получить статистику для тегов с id={}. " +
                    "Причина: {}", tagIds, cause.getClass().getSimpleName(), cause);
            return Map.of();
        }

        @Override
        public void deleteTagAnalytics(UUID id) {
            if (cause instanceof TagNotFoundException) {
                log.debug("Тег с id={} не найден в Tag Analytics (404). Возвращаем пустую статистику.", id);
            }

            if (cause instanceof TagAnalyticsClientException) {
                log.warn("Ошибка клиента при получении статистики для тега id={}: {}", id, cause.getMessage());
                throw (TagAnalyticsClientException) cause;
            }

            log.warn("Сервис Tag Analytics недоступен. Не удалось удалить статистику для тега с id={}. " +
                    "Причина: {}", id, cause.getClass().getSimpleName(), cause);
        }
    }
}
