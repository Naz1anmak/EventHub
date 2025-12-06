package ru.practicum.eventhub.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.eventhub.domain.model.Tag;

import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {

    @EntityGraph(attributePaths = {"events"})
    Page<Tag> findAll(Pageable pageable);
}
