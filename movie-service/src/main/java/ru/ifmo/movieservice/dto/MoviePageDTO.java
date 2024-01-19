package ru.ifmo.movieservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
