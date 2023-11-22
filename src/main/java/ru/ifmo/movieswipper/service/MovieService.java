package ru.ifmo.movieswipper.service;

import info.movito.themoviedbapi.TmdbMovies;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.movieswipper.mapper.MovieMapper;
import ru.ifmo.movieswipper.model.Movie;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final TmdbMovies tmdbMovies;

    public List<Movie> getPopulars() {
        return tmdbMovies.getPopularMovies("en", 1)
                .getResults()
                .stream()
                .map(MovieMapper::toDomain)
                .toList();
    }

}
