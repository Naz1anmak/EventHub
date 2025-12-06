package ru.practicum.eventhub.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.eventhub.domain.model.Event;
import ru.practicum.eventhub.domain.model.User;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    @EntityGraph(attributePaths = {"category", "createdBy", "tags"})
    Page<Event> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"category", "createdBy", "tags"})
    Page<Event> findByCreatedBy(User user, Pageable pageable);
}
