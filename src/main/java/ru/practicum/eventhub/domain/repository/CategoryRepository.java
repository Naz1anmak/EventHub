package ru.practicum.eventhub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.eventhub.domain.model.Category;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByNameIgnoreCase(String name);
}
