package com.desarrollox.backend.core.exception;

public class NoClientAccessException extends RuntimeException{
    public NoClientAccessException(){
        super("No tienes acceso a esta ruta, solo para clientes");
    }
}
