package ru.ifmo.fileservice.service;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import ru.ifmo.fileservice.exception.StorageException;
import ru.ifmo.fileservice.model.FileDB;
import ru.ifmo.fileservice.model.User;
import ru.ifmo.fileservice.repository.FileRepository;

@Service
@RequiredArgsConstructor
public class FileService {

    private FileRepository fileRepository;

    public void store(MultipartFile file, User user) throws StorageException {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file");
        }

        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            byte[] bytes = file.getBytes();

            FileDB fileDB = FileDB.builder()
                .filename(fileName)
                .user(user)
                .data(bytes).build();

            fileRepository.save(fileDB);
        } catch (IOException ex) {
            throw new StorageException("Failed to store file", ex);
        }
    }

    public FileDB getFile(String id) throws NoSuchElementException{
        return fileRepository.findById(id).orElseThrow();
    }

    public Resource getFileAsResource(FileDB file) throws NoSuchElementException {
        return new ByteArrayResource(file.getData());
    }


}
