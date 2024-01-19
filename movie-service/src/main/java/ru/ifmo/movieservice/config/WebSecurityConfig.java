package ru.ifmo.movieservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import lombok.RequiredArgsConstructor;
import ru.ifmo.movieservice.filter.AuthenticationAdderFilter;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final AuthenticationAdderFilter filter;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(
                                "/movie/**",
                                "/movie/movies-by-genre/**",
                                "/movie/popular-movies/**",
                                "/movie/genres-list"
                            ).authenticated()
                        .anyExchange().permitAll())
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .addFilterAt(filter, SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }
}
