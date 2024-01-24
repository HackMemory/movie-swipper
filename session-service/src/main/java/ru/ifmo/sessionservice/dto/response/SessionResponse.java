package ru.ifmo.sessionservice.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.ifmo.sessionservice.dto.UserDTO;

@Data
@AllArgsConstructor
@Builder
public class SessionResponse {
    private Long id;
    private String code;
    private List<UserDTO> users;
}