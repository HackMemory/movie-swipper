package ru.ifmo.movieswipper.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank
    @Size(min = 4, max = 25)
    private String username;

    @NotBlank
    private String password;
}