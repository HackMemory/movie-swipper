package ru.ifmo.sessionservice.exception;

public class PermissionDeniedException extends RuntimeException{
    public PermissionDeniedException(String message){
        super(message);
    }
}
