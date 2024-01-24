package ru.ifmo.sessionservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MovieMatchedDTO {
    private Long tmdbMovieId;
    private List<UserDTO> users;
    private boolean liked;
}
