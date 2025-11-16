package ru.practicum.springtest.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.springtest.domain.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);
}
