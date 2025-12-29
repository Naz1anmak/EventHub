package ru.practicum.eventhub.domain.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.eventhub.domain.model.Category;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Override
    @EntityGraph(attributePaths = {"projects"})
    @NonNull
    Page<Category> findAll(@NonNull Pageable pageable);

    boolean existsByName(String name);
}
