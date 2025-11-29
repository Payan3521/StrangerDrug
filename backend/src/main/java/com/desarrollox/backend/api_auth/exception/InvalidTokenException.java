package com.desarrollox.backend.api_auth.exception;

//solo serviria para cuando hagamos el refresh token si esta expirado o invalido
public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(){
        super("Token inválido o expirado");
    }
    public InvalidTokenException(String message){
        super(message);
    }
}
