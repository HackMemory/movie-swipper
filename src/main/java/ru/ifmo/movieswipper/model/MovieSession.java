package ru.ifmo.movieswipper.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import lombok.*;

@Entity(name = "movie_session")
@RequiredArgsConstructor
@Getter
@Setter
public class MovieSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Max(64)
    private String imdb_movie_id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Session session;

    private Boolean liked;
}