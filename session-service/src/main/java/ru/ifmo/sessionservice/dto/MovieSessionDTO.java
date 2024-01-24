package ru.ifmo.sessionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MovieSessionDTO {
    private Long tmdbMovieId;
    private boolean liked;
}
