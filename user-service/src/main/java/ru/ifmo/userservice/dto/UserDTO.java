package ru.ifmo.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.ifmo.userservice.model.Role;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private Set<Role> roles;
}
