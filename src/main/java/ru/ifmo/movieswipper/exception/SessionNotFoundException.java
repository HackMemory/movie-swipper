package ru.ifmo.movieswipper.exception;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(String message){
        super(message);
    }
}
