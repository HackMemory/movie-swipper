package ru.ifmo.movieswipper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.movieswipper.model.MovieSession;
import ru.ifmo.movieswipper.repository.MovieSessionRepository;

@Service
@RequiredArgsConstructor
public class MovieSessionService {
    private MovieSessionRepository movieSessionRepository;

    public void save(MovieSession movieSession){
        movieSessionRepository.save(movieSession);
    }

    public void setLikeStatus(boolean like){

    }

    public void getMovieList(){

    }
}
