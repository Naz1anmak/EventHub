package ru.practicum.eventhub.domain.event;

import java.util.UUID;

public record CategoryDeletedEvent(UUID categoryId) {
}
