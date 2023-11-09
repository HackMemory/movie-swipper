package ru.ifmo.movieswipper.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "movie_session")
@RequiredArgsConstructor
@Getter
@Setter
public class MovieSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Session session;

    private Boolean liked;
}