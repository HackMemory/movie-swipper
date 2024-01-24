package ru.ifmo.sessionservice.model;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity(name = "movie_session")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MovieSession {
    @Id
    private Long id;

    @NotNull
    private Long tmdbMovieId;

    private User user;

    private Session session;

    private Boolean liked;
}