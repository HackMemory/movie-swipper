package ru.ifmo.movieswipper.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "user_session")
@RequiredArgsConstructor
@Getter
@Setter
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Session session;
}
