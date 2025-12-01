package com.desarrollox.backend.api_models.exception;

public class ModelNotFoundException extends RuntimeException {
    
    public ModelNotFoundException(String message) {
        super(message);
    }
}