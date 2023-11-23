package ru.ifmo.movieswipper.exception;

public class SessionNotFound extends RuntimeException {
    public SessionNotFound(String message){
        super(message);
    }
}
