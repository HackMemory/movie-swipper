package ru.ifmo.gateway.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ValidateTokenRequest(@NotBlank String token) {
}
