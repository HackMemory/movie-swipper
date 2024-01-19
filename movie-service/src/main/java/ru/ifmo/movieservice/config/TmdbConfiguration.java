package ru.ifmo.movieservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import info.movito.themoviedbapi.TmdbApi;

@Configuration
public class TmdbConfiguration {

    @Value("${tmdb.api_key}")
    private String apiKey;

    @Bean
    public TmdbApi tmdbMovies() {
        return new TmdbApi(this.apiKey);
    }
}
