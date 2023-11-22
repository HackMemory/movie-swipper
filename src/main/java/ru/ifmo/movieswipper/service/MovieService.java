package ru.ifmo.movieswipper.service;

import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.ifmo.movieswipper.dto.MovieDTO;
import ru.ifmo.movieswipper.dto.MoviePageDTO;
import ru.ifmo.movieswipper.mapper.MovieMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final TmdbMovies tmdbMovies;

    public MoviePageDTO getPopulars(Pageable pageable) {
        MovieResultsPage popularMoviesPage = tmdbMovies.getPopularMovies("ru", pageable.getPageNumber());

        return MoviePageDTO.builder()
                .page(popularMoviesPage.getPage())
                .totalPages(popularMoviesPage.getTotalPages())
                .totalResults(popularMoviesPage.getTotalResults())
                .results(popularMoviesPage.getResults().stream()
                        .map(MovieMapper.INSTANCE::toDomain)
                        .collect(Collectors.toList()))
                .build();
    }


    public Optional<MovieDTO> getMovie(Integer id) {
        return Optional.ofNullable(MovieMapper.INSTANCE.toDomain(tmdbMovies.getMovie(id, "ru")));
    }

}
