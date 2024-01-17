package ru.ifmo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@OpenAPIDefinition(info = @Info(title = "My Rest API", version = "v1"))
@EnableReactiveFeignClients
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

}
