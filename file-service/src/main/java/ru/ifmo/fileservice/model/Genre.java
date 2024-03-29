package ru.ifmo.fileservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Table(name = "genre")
@RequiredArgsConstructor
@Getter
@Setter
public class Genre {
    @Id
    private Long id;
    private String name;
    private Long tmdb_genre_id;
}