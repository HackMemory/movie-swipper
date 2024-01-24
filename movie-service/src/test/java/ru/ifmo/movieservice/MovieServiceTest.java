package ru.ifmo.movieservice;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbDiscover;
import info.movito.themoviedbapi.TmdbGenre;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import reactor.core.publisher.Mono;
import ru.ifmo.movieservice.dto.MoviePageDTO;
import ru.ifmo.movieservice.service.MovieService;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private TmdbApi tmdbApi;

    @InjectMocks
    private MovieService movieService;

    @Test
    void testGetPopulars() {
        MovieResultsPage movieResultsPage = mock(MovieResultsPage.class);

        TmdbMovies tmdbMovies = mock(TmdbMovies.class);
        when(tmdbApi.getMovies()).thenReturn(tmdbMovies);
        when(tmdbMovies.getPopularMovies("ru", 1)).thenReturn(movieResultsPage);

        Mono<Optional<MoviePageDTO>> result = movieService.getPopulars(1);

        assertTrue(result.block().isPresent());
    }

    @Test
    void testGetMoviesByGenres() {
        String genres = "28,12";
        MovieResultsPage movieResultsPage = mock(MovieResultsPage.class);

        TmdbDiscover tmdbDiscover = mock(TmdbDiscover.class);
        when(tmdbApi.getDiscover()).thenReturn(tmdbDiscover);
        when(tmdbApi.getDiscover().getDiscover(any())).thenReturn(movieResultsPage);


        MovieService movieService = new MovieService(tmdbApi);
        Mono<Optional<MoviePageDTO>> result = movieService.getMoviesByGenres(1, genres);

        assertTrue(result.block().isPresent());
    }

    @Test
    void testGetGenresList() {
        List<Genre> genreList = mock(List.class);

        TmdbGenre tmdbGenre = mock(TmdbGenre.class);
        when(tmdbApi.getGenre()).thenReturn(tmdbGenre);
        when(tmdbGenre.getMovieGenreList("ru")).thenReturn(genreList);

        MovieService movieService = new MovieService(tmdbApi);
        Mono<Optional<List<Genre>>> result = movieService.getGenresList();

        assertTrue(result.block().isPresent());
    }
}
