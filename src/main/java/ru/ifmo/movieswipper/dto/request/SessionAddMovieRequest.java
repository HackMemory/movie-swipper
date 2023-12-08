package ru.ifmo.movieswipper.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionAddMovieRequest {
    @NotNull
    private Long tmdbMovieId;

    @NotNull
    private Boolean liked;
}
