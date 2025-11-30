package com.desarrollox.backend.core.handler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.desarrollox.backend.api_auth.exception.InvalidCredentialsException;
import com.desarrollox.backend.api_auth.exception.InvalidTokenException;
import com.desarrollox.backend.api_photos.exception.PhotoNotFoundException;
import com.desarrollox.backend.api_register.exception.UserNotFoundException;
import com.desarrollox.backend.api_videos.exception.VideoNotFoundException;
import com.desarrollox.backend.core.exception.NoAdminAccessException;
import com.desarrollox.backend.core.exception.NoClientAccessException;
import com.desarrollox.backend.core.exception.NoTokenException;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(
        {
            NoTokenException.class,
            NoAdminAccessException.class,
            NoClientAccessException.class,
            InvalidTokenException.class,
            InvalidCredentialsException.class
        }
    )
    public ResponseEntity<Map<String, Object>> handleUnauthorized(RuntimeException e) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", e.getMessage());
    }

    @ExceptionHandler(
        {
            UserNotFoundException.class,
            PhotoNotFoundException.class,
            VideoNotFoundException.class
        }
    )
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage());
    }
}
