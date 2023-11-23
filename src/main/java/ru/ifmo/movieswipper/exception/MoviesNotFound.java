package ru.ifmo.movieswipper.exception;

public class MoviesNotFound extends RuntimeException {
    public MoviesNotFound(String message){
        super(message);
    }
}