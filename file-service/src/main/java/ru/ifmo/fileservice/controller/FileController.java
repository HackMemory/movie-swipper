package ru.ifmo.fileservice.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import ru.ifmo.fileservice.dto.FileDTO;
import ru.ifmo.fileservice.exception.StorageException;
import ru.ifmo.fileservice.model.FileDB;
import ru.ifmo.fileservice.model.User;
import ru.ifmo.fileservice.service.FileService;

@RestController
@RequestMapping("files")
@RequiredArgsConstructor
public class FileController {

    @Autowired
    private final FileService fileService;

    @Hidden
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("userID") Long userID) {
        try {
            FileDTO fileDTO = fileService.store(file, User.builder().id(userID).build());
            return ResponseEntity.ok(fileDTO);
        } catch (StorageException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable String id){
        try{
            FileDB file = fileService.getFile(id);
            Resource fileResource = fileService.getFileAsResource(file);

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(fileResource);
        }catch (NoSuchElementException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
