package ru.ifmo.movieswipper.repository;

import org.springframework.data.repository.CrudRepository;
import ru.ifmo.movieswipper.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}