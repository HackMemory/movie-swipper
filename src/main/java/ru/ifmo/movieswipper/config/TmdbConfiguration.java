package ru.ifmo.movieswipper.config;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TmdbConfiguration {

    @Value("${tmdb.api_key}")
    private String apiKey;

    @Bean
    public TmdbMovies tmdbMovies() {
        return new TmdbApi(this.apiKey).getMovies();
    }
}
