package ru.ifmo.sessionservice.exception;

public class UserExistInSessionException extends RuntimeException {
    public UserExistInSessionException(String message){
        super(message);
    }
}
