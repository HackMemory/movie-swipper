package ru.ifmo.movieswipper.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.ifmo.movieswipper.dto.UserDTO;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class SessionResponse {
    private Long id;
    private String code;
    private List<UserDTO> users;
}