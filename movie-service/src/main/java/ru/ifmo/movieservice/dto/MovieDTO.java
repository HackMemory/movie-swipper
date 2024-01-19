package ru.ifmo.movieservice.dto;

import java.time.LocalDate;
import java.util.List;

import info.movito.themoviedbapi.model.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MovieDTO {
    private String id;
    private String title;
    private String synopsys;
    private float rate;
    private LocalDate releaseDate;
    private List<Genre> genres;
}