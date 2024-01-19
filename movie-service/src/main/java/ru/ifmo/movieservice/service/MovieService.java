package ru.ifmo.movieservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.Discover;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.ifmo.movieservice.dto.MovieDTO;
import ru.ifmo.movieservice.dto.MoviePageDTO;
import ru.ifmo.movieservice.mapper.MovieMapper;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final TmdbApi tmdbApi;

    public Mono<Optional<MoviePageDTO>> getPopulars(int page) {
        MovieResultsPage popularMoviesPage = tmdbApi.getMovies().getPopularMovies("ru", page);

        return Mono.fromCallable(() -> Optional.ofNullable(MoviePageDTO.builder()
                .page(popularMoviesPage.getPage())
                .totalPages(popularMoviesPage.getTotalPages())
                .totalResults(popularMoviesPage.getTotalResults())
                .results(popularMoviesPage.getResults().stream()
                        .map(MovieMapper.INSTANCE::toDomain)
                        .collect(Collectors.toList()))
                .build()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Optional<MoviePageDTO>> getMoviesByGenres(int page, String genres) {
        Discover discover = new Discover();
        discover.page(page)
                .sortBy("popularity.desc")
                .withGenres(genres).language("ru");


        MovieResultsPage popularMoviesPage = tmdbApi.getDiscover().getDiscover(discover);
        return Mono.fromCallable(() -> Optional.ofNullable(MoviePageDTO.builder()
                .page(popularMoviesPage.getPage())
                .totalPages(popularMoviesPage.getTotalPages())
                .totalResults(popularMoviesPage.getTotalResults())
                .results(popularMoviesPage.getResults().stream()
                        .map(MovieMapper.INSTANCE::toDomain)
                        .collect(Collectors.toList()))
                .build()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Optional<List<Genre>>> getGenresList(){
        return Mono.fromCallable(() -> Optional.ofNullable(tmdbApi.getGenre().getMovieGenreList("ru")))
                    .subscribeOn(Schedulers.boundedElastic());
    }


    public Mono<Optional<MovieDTO>> getMovie(Integer id) {
        return Mono.fromCallable(() -> Optional.ofNullable(MovieMapper.INSTANCE.toDomain(tmdbApi.getMovies().getMovie(id, "ru"))))
                    .subscribeOn(Schedulers.boundedElastic());
    }

}
