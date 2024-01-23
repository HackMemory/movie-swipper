package ru.ifmo.fileservice.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.ifmo.fileservice.model.FileDB;
import ru.ifmo.fileservice.model.User;

@Repository
public interface FileRepository extends CrudRepository<FileDB, String> {
    Optional<FileDB> findByUser(User user);
}
