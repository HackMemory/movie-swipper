package ru.ifmo.movieswipper.dto;

import info.movito.themoviedbapi.model.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

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