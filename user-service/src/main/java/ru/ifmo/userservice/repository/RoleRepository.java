package ru.ifmo.userservice.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.ifmo.userservice.model.Role;
@Repository
public interface  RoleRepository extends CrudRepository<Role, String> {
    Optional<Role> findByName(String name);
}
