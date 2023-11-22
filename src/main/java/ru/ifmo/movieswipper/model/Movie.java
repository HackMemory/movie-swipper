package ru.ifmo.movieswipper.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Movie {

    @NotBlank
    private String id;
    @NotBlank
    private String title;
    @NotBlank
    private String synopsys;
    @NotNull
    private Rate rate;
    @NotNull
    private LocalDate releaseDate;
    private Person director;
    private List<Person> actors;
}