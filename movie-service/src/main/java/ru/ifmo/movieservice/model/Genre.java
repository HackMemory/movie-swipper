package ru.ifmo.movieservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class Genre {
    private Long id;
    private String name;
    private Long tmdb_genre_id;
}