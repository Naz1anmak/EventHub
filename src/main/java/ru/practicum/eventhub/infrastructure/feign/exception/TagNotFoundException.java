package ru.practicum.eventhub.infrastructure.feign.exception;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException(String message) {
        super(message);
    }
}
