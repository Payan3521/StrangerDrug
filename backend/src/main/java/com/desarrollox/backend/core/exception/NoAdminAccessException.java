package com.desarrollox.backend.core.exception;

public class NoAdminAccessException extends RuntimeException{
    public NoAdminAccessException(){
        super("No tienes acceso a esta ruta, solo para administradores");
    }
}
