package ru.ifmo.movieswipper.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Entity(name = "session")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User creator;

    @NotNull
    private String inviteCode;
}