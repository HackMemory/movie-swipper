package ru.ifmo.authservice.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record ValidateTokenResponse(String username, List<String> authorities) {
}
