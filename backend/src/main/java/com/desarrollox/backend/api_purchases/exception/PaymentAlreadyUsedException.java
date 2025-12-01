package com.desarrollox.backend.api_purchases.exception;

public class PaymentAlreadyUsedException extends RuntimeException {
    
    public PaymentAlreadyUsedException(String message) {
        super(message);
    }
}
