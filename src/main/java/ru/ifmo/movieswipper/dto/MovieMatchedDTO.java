package ru.ifmo.movieswipper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MovieMatchedDTO {
    private Long tmdbMovieId;
    private List<UserDTO> users;
    private boolean liked;
}
