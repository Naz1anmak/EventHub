package ru.practicum.eventhub.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.eventhub.model.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {

    @Override
    @EntityGraph(attributePaths = {"events"})
    @NonNull
    Page<Tag> findAll(@NonNull Pageable pageable);

    Page<Tag> findAllByEventsId(UUID eventId, Pageable pageable);

    Optional<Tag> findByIdAndEventsId(UUID tagId, UUID eventId);

    boolean existsByName(String name);

    boolean existsByIdAndEventsId(UUID tagId, UUID eventId);

    List<Tag> findAllByIdInAndEventsId(Set<UUID> tagIds, UUID eventId);
}
