package ru.ifmo.userservice.config;


import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.codec.ErrorDecoder;
import ru.ifmo.userservice.error.CustomErrorDecoder;

public class FeignConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder(new ObjectMapper());
    }
}
