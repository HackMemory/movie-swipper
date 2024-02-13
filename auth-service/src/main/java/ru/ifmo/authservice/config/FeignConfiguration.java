package ru.ifmo.authservice.config;


import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.codec.ErrorDecoder;
import ru.ifmo.authservice.error.CustomErrorDecoder;

public class FeignConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder(new ObjectMapper());
    }
}
