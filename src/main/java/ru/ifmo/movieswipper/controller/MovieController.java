package ru.ifmo.movieswipper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.ifmo.movieswipper.service.MovieService;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/populars")
    public ResponseEntity<?> getPopular(Pageable pageable) {
        try {
            return ResponseEntity.ok(movieService.getPopulars(pageable));
        } catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping("/getMovie/{id}")
    public ResponseEntity<?> getMovie(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(movieService.getMovie(Integer.parseInt(id)));
        } catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }

    }
}
