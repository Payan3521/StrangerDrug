package com.desarrollox.backend.api_purchases.exception;

public class PurchaseAlreadyRegisteredException extends RuntimeException {
    
    public PurchaseAlreadyRegisteredException(String message) {
        super(message);
    }
}
