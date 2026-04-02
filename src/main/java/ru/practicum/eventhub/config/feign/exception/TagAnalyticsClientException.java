package ru.practicum.eventhub.config.feign.exception;

public class TagAnalyticsClientException extends RuntimeException {
    public TagAnalyticsClientException(String message) {
        super(message);
    }
}
