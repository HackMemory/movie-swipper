package ru.ifmo.fileservice.service;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import ru.ifmo.fileservice.dto.FileDTO;
import ru.ifmo.fileservice.exception.StorageException;
import ru.ifmo.fileservice.model.FileDB;
import ru.ifmo.fileservice.model.User;
import ru.ifmo.fileservice.repository.FileRepository;

@Service
@RequiredArgsConstructor
public class FileService {

    @Autowired
    private final FileRepository fileRepository;

    public FileDTO store(MultipartFile file, User user) throws StorageException {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file");
        }

        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            byte[] bytes = file.getBytes();

            Optional<FileDB> fileDBOptional = fileRepository.findByUser(user);
            if(fileDBOptional.isPresent()){
                fileRepository.delete(fileDBOptional.get());
            }

            FileDB fileDB = FileDB.builder()
                .filename(fileName)
                .user(user)
                .data(bytes).build();

            fileDB = fileRepository.save(fileDB);
            return new FileDTO(fileDB.getUuid());
        } catch (IOException ex) {
            throw new StorageException("Failed to store file", ex);
        }
    }

    public FileDB getFile(String id) throws NoSuchElementException{
        return fileRepository.findById(id).orElseThrow(
            () -> new NoSuchElementException("File not found")
        );
    }

    public Resource getFileAsResource(FileDB file) throws NoSuchElementException {
        return new ByteArrayResource(file.getData());
    }


}
