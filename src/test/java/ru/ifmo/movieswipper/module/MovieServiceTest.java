package ru.ifmo.movieswipper.module;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbDiscover;
import info.movito.themoviedbapi.TmdbGenre;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.ifmo.movieswipper.dto.MoviePageDTO;
import ru.ifmo.movieswipper.service.MovieService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private TmdbApi tmdbApi;

    @InjectMocks
    private MovieService movieService;

    @Test
    void testGetPopulars() {
        Pageable pageable = mock(Pageable.class);
        MovieResultsPage movieResultsPage = mock(MovieResultsPage.class);

        TmdbMovies tmdbMovies = mock(TmdbMovies.class);
        when(tmdbApi.getMovies()).thenReturn(tmdbMovies);
        when(tmdbMovies.getPopularMovies("ru", pageable.getPageNumber())).thenReturn(movieResultsPage);

        Optional<MoviePageDTO> result = movieService.getPopulars(pageable);

        assertTrue(result.isPresent());
    }

    @Test
    void testGetMoviesByGenres() {
        Pageable pageable = mock(Pageable.class);
        String genres = "28,12";
        MovieResultsPage movieResultsPage = mock(MovieResultsPage.class);

        TmdbDiscover tmdbDiscover = mock(TmdbDiscover.class);
        when(tmdbApi.getDiscover()).thenReturn(tmdbDiscover);
        when(tmdbApi.getDiscover().getDiscover(any())).thenReturn(movieResultsPage);


        MovieService movieService = new MovieService(tmdbApi);
        Optional<MoviePageDTO> result = movieService.getMoviesByGenres(pageable, genres);

        assertTrue(result.isPresent());
    }

    @Test
    void testGetGenresList() {
        List<Genre> genreList = mock(List.class);

        TmdbGenre tmdbGenre = mock(TmdbGenre.class);
        when(tmdbApi.getGenre()).thenReturn(tmdbGenre);
        when(tmdbGenre.getMovieGenreList("ru")).thenReturn(genreList);

        MovieService movieService = new MovieService(tmdbApi);
        Optional<List<Genre>> result = movieService.getGenresList();

        assertTrue(result.isPresent());
    }
}
