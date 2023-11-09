package ru.ifmo.movieswipper.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity(name = "genre")
@RequiredArgsConstructor
@Getter
@Setter
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long imdb_id;
}