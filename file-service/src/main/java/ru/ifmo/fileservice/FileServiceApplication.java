package ru.ifmo.fileservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = ReactiveUserDetailsServiceAutoConfiguration.class)
public class FileServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileServiceApplication.class, args);
	}

}
