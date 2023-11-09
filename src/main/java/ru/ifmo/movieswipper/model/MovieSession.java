package ru.ifmo.movieswipper.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private Long imdb_movie_id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Session session;

    private Boolean liked;
}