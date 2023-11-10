package ru.ifmo.movieswipper.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.movieswipper.model.Role;

import java.util.Optional;
@Repository
public interface  RoleRepository extends CrudRepository<Role, String> {
    Optional<Role> findByName(String name);
}
