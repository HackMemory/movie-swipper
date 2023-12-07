package ru.ifmo.movieswipper.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.ifmo.movieswipper.service.MovieService;

@RestController
@RequestMapping("api/v1/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/getPopularMovies")
    public ResponseEntity<?> getPopular(Pageable pageable) {
        try {
            return ResponseEntity.ok(movieService.getPopulars(pageable));
        } catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping("/getMoviesByGenre")
    public ResponseEntity<?> getByGenres(Pageable pageable, String genres) {
        try {
            return ResponseEntity.ok(movieService.getMoviesByGenres(pageable, genres));
        } catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping("/getGenresList")
    public ResponseEntity<?> getGenresList() {
        try {
            return ResponseEntity.ok(movieService.getGenresList());
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
