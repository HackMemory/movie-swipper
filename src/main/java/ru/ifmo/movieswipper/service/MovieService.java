package ru.ifmo.movieswipper.service;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.ifmo.movieswipper.dto.MovieDTO;
import ru.ifmo.movieswipper.dto.MoviePageDTO;
import ru.ifmo.movieswipper.mapper.MovieMapper;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final TmdbApi tmdbApi;

    public MoviePageDTO getPopulars(Pageable pageable) {
        MovieResultsPage popularMoviesPage = tmdbApi.getMovies().getPopularMovies("ru", pageable.getPageNumber());

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
        return Optional.ofNullable(MovieMapper.INSTANCE.toDomain(tmdbApi.getMovies().getMovie(id, "ru")));
    }

}
