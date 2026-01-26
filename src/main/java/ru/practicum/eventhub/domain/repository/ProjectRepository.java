package ru.practicum.eventhub.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.eventhub.domain.model.Project;

import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @EntityGraph(attributePaths = {"category", "owner"})
    Page<Project> findAllByCategoryId(UUID categoryId, Pageable pageable);

    @EntityGraph(attributePaths = {"category", "owner"})
    Optional<Project> findByIdAndCategoryId(UUID projectId, UUID categoryId);

    boolean existsByName(String name);
}
