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
        try {
            
            return movieService.getPopulars(page);
        } catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping("/movies-by-genre")
    public Mono<?> getByGenres(
            @RequestParam(value = "page", required=false, defaultValue = "0") Integer page,
            String genres
        ) {

        try {
            return movieService.getMoviesByGenres(page, genres);
        } catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping("/genres")
    public Mono<?> getGenresList() {
        try {
            return movieService.getGenresList();
        } catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping("/{id}")
    public Mono<?> getMovie(@PathVariable("id") String id) {
        try {
            return movieService.getMovie(Integer.parseInt(id));
        } catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }

    }
}
