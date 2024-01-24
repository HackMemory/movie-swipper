package ru.ifmo.sessionservice.exception;

public class MoviesNotFound extends RuntimeException {
    public MoviesNotFound(String message){
        super(message);
    }
}