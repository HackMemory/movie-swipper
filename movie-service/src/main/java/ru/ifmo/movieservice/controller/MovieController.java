package ru.ifmo.movieservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.ifmo.movieservice.service.MovieService;

@RestController
@RequestMapping("movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/popular-movies")
    public Mono<?> getPopular(
                @RequestParam(value = "page", required=false, defaultValue = "0") Integer page
            )
    {            
        return movieService.getPopulars(page)
            .onErrorMap(Exception.class, e -> new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @GetMapping("/movies-by-genre")
    public Mono<?> getByGenres(
            @RequestParam(value = "page", required=false, defaultValue = "0") Integer page,
            String genres) {
        return movieService.getMoviesByGenres(page, genres)
            .onErrorMap(Exception.class, e -> new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @GetMapping("/genres")
    public Mono<?> getGenresList() {
        return movieService.getGenresList()
            .onErrorMap(Exception.class, e -> new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @GetMapping("/{id}")
    public Mono<?> getMovie(@PathVariable("id") String id) {
        return movieService.getMovie(Integer.parseInt(id))
            .onErrorMap(Exception.class, e -> new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()));
    }
}
