package ru.practicum.springtest.service;

import ru.practicum.springtest.domain.User;

public interface UserService {

    User findByUsername(String username);
}
