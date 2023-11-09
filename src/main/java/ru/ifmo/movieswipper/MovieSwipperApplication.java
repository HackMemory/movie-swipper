package ru.ifmo.movieswipper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class MovieSwipperApplication {
	public static void main(String[] args) {
		SpringApplication.run(MovieSwipperApplication.class, args);
	}

}
