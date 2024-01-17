package ru.ifmo.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ValidateTokenRequest(@NotBlank String token) {
}
