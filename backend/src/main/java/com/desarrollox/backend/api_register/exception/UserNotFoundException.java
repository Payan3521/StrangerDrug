package com.desarrollox.backend.api_register.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(){
        super("Usuario no encontrado");
    }
}