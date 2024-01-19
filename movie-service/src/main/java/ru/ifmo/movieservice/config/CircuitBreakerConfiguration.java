package ru.ifmo.movieservice.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CircuitBreakerConfiguration {
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @Bean
    public CircuitBreakerConfig circuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(5)
                .failureRateThreshold(40)
                .waitDurationInOpenState(Duration.ofMillis(10000))
                .permittedNumberOfCallsInHalfOpenState(2)
                .build();
    }

    @Bean
    public CircuitBreaker userServiceClientCircuitBreaker(CircuitBreakerConfig circuitBreakerConfig) {
        return circuitBreakerRegistry.circuitBreaker("UserServiceClientCB", circuitBreakerConfig);
    }
}
