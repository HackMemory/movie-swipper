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
public class MoviePageDTO {
    private List<MovieDTO> results;
    private int page;
    private int totalPages;
    private int totalResults;
}
