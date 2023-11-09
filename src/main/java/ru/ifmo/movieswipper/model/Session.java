package ru.ifmo.movieswipper.model;

import jakarta.persistence.*;
import lombok.*;


@Entity(name = "session")
@RequiredArgsConstructor
@Getter
@Setter
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User creator;

    private String inviteCode;
}