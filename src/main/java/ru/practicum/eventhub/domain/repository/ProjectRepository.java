package ru.practicum.eventhub.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.eventhub.domain.model.Project;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @EntityGraph(attributePaths = {"category", "owner"})
    Page<Project> findAll(Pageable pageable);
}
