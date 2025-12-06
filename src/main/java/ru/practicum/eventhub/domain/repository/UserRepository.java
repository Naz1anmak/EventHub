package ru.practicum.eventhub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.eventhub.domain.model.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
