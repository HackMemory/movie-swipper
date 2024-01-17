package ru.ifmo.gateway.client;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import ru.ifmo.gateway.dto.request.ValidateTokenRequest;
import ru.ifmo.gateway.dto.response.ValidateTokenResponse;

@Component
@ReactiveFeignClient(name = "auth-service", path = "${context-path}")
public interface AuthServiceClient {
    @PostMapping("/token/validate")
    @CircuitBreaker(name = "AuthServiceClientCB")
    Mono<ValidateTokenResponse> validateToken(@RequestBody ValidateTokenRequest request);
}
