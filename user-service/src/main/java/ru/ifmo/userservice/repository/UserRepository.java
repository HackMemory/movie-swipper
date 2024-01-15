package ru.ifmo.userservice.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ru.ifmo.userservice.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
