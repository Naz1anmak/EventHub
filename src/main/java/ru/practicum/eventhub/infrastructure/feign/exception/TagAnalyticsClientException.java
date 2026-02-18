package ru.practicum.eventhub.infrastructure.feign.exception;

public class TagAnalyticsClientException extends RuntimeException {
    public TagAnalyticsClientException(String message) {
        super(message);
    }
}
