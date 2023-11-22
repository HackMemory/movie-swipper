package ru.ifmo.movieswipper.dto;

import info.movito.themoviedbapi.model.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.ifmo.movieswipper.model.Person;

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
    private Person director;
    private List<Person> actors;
    private List<Genre> genres;
}