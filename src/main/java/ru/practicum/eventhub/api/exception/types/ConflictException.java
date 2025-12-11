package ru.practicum.eventhub.api.exception.types;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
