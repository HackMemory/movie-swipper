package ru.ifmo.movieswipper.exception;

public class PermissionDeniedException extends RuntimeException{
    public PermissionDeniedException(String message){
        super(message);
    }
}
