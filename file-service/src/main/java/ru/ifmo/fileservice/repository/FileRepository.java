package ru.ifmo.fileservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.ifmo.fileservice.model.FileDB;

@Repository
public interface FileRepository extends CrudRepository<FileDB, String> {
    
}
