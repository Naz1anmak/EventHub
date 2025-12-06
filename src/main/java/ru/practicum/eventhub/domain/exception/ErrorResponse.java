package ru.practicum.eventhub.domain.exception;

public record ErrorResponse(
        String path,
        String httpMethod,
        int statusCode,
        String errorReason,
        String message
) {
}
