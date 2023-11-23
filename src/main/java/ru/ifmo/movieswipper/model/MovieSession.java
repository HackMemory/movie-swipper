package ru.ifmo.movieswipper.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import lombok.*;

@Builder
@Entity(name = "movie_session")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MovieSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long tmdbMovieId;

    @ManyToOne
    private User user;

    @ManyToOne
    private Session session;

    private Boolean liked;
}