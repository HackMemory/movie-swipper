package ru.ifmo.movieswipper.config;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.tools.WebBrowser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TmdbConfiguration {

    @Value("${tmdb.api_key}")
    private String apiKey;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    public TmdbApi tmdbMovies() {
        if (activeProfile.equals("prod")) {
            return new TmdbApi(this.apiKey);
        }else {
            WebBrowser wb = new WebBrowser();
            wb.setProxy("127.0.0.1", "2080", "", "");

            return new TmdbApi(this.apiKey, wb, true);
        }
    }
}
