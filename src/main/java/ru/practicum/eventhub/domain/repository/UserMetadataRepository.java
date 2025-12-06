package ru.practicum.eventhub.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.eventhub.domain.model.UserMetadata;

import java.util.UUID;

public interface UserMetadataRepository extends JpaRepository<UserMetadata, UUID> {

    @EntityGraph(attributePaths = "user")
    Page<UserMetadata> findAll(Pageable pageable);
}
