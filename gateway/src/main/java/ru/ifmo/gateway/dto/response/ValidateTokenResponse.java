package ru.ifmo.gateway.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record ValidateTokenResponse(String username, List<String> authorities) {
}
