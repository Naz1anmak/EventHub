package ru.practicum.eventhub.domain.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.eventhub.domain.model.UserMetadata;

import java.util.Optional;
import java.util.UUID;

public interface UserMetadataRepository extends JpaRepository<UserMetadata, UUID> {

    @Override
    @EntityGraph(attributePaths = "user")
    @NonNull
    Page<UserMetadata> findAll(@NonNull Pageable pageable);

    Optional<UserMetadata> findByUserId(UUID userId);

    boolean existsByPhone(String phone);
}
