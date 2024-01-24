package ru.ifmo.sessionservice.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.ifmo.sessionservice.model.Genre;
import ru.ifmo.sessionservice.model.Role;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private Set<Role> roles;
    private Set<Genre> genres;
}
