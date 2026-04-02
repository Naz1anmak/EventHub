package ru.practicum.eventhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.eventhub.model.CompensationAction;

import java.util.UUID;

public interface CompensationActionRepository extends JpaRepository<CompensationAction, UUID> {
}
