package ru.ifmo.sessionservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SessionCreateResponse {
    private String code;
}
