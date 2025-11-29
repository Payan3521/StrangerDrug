package com.desarrollox.backend.core.exception;

public class NoTokenException extends RuntimeException{
    public NoTokenException() {
        super("Debes ingresar un token de acceso");
    }
}
