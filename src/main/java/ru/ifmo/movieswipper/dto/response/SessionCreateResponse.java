package ru.ifmo.movieswipper.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SessionCreateResponse {
    String code;
}