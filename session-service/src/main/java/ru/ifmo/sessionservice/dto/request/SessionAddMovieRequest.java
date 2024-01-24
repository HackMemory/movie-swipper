package ru.ifmo.sessionservice.dto.request;

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
