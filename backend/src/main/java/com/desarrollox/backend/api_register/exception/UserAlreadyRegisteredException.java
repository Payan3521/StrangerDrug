package com.desarrollox.backend.api_register.exception;

public class UserAlreadyRegisteredException extends RuntimeException {
    
    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}
