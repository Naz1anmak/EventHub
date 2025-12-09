package ru.practicum.eventhub.api.exception;

public record ErrorResponse(
        String path,
        String httpMethod,
        int statusCode,
        String errorReason,
        String message
) {
}
