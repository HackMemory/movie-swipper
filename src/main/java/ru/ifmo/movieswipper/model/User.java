package ru.ifmo.movieswipper.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity(name = "user")
@RequiredArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;

    @OneToMany
    @JoinColumn(name = "genre")
    private Set<Genre> genres;
}