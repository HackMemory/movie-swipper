package ru.ifmo.movieswipper.exception;

public class UserExistInSessionException extends RuntimeException {
    public UserExistInSessionException(String message){
        super(message);
    }
}
