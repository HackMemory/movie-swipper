package ru.ifmo.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import ru.ifmo.userservice.dto.FileDTO;


@FeignClient(name = "file-service", path = "${server.servlet.context-path}")
public interface FileServiceClient {
    @PostMapping(value = "/files/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CircuitBreaker(name = "FileServiceClientCB")
    ResponseEntity<FileDTO> uploadFile(@RequestPart("file") MultipartFile file, @RequestParam("userID") Long userID);
}
