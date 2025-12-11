package ru.practicum.eventhub.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.eventhub.domain.model.Tag;

import java.util.Optional;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {

    @Override
    @EntityGraph(attributePaths = {"events"})
    Page<Tag> findAll(Pageable pageable);

    Page<Tag> findAllByEventsId(UUID eventId, Pageable pageable);

    Optional<Tag> findByIdAndEventsId(UUID tagId, UUID eventId);
}
