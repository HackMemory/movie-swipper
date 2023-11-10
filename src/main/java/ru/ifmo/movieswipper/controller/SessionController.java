package ru.ifmo.movieswipper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.movieswipper.model.Session;

import java.util.Optional;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController{

}
