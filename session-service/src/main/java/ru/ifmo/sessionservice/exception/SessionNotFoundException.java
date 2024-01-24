package ru.ifmo.sessionservice.exception;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(String message){
        super(message);
    }
}
