package ru.practicum.eventhub.domain.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.eventhub.domain.model.Event;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    @Override
    @EntityGraph(attributePaths = {"tags"})
    @NonNull
    Page<Event> findAll(@NonNull Pageable pageable);
}
