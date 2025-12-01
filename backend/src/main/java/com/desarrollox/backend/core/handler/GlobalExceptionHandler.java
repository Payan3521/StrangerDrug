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
import com.desarrollox.backend.api_models.exception.ModelNotFoundException;
import com.desarrollox.backend.api_notifications.exception.NotificationNotFoundException;
import com.desarrollox.backend.api_photos.exception.PhotoNotFoundException;
import com.desarrollox.backend.api_purchases.exception.PaymentAlreadyUsedException;
import com.desarrollox.backend.api_purchases.exception.PurchaseAlreadyRegisteredException;
import com.desarrollox.backend.api_purchases.exception.PurchaseNotFoundException;
import com.desarrollox.backend.api_register.exception.InvalidAgeException;
import com.desarrollox.backend.api_register.exception.UserAlreadyRegisteredException;
import com.desarrollox.backend.api_register.exception.UserNotFoundException;
import com.desarrollox.backend.api_sections.exception.SectionAlreadyRegisteredException;
import com.desarrollox.backend.api_sections.exception.SectionNotFoundException;
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
            VideoNotFoundException.class,
            SectionNotFoundException.class,
            ModelNotFoundException.class,
            NotificationNotFoundException.class,
            PurchaseNotFoundException.class
        }
    )
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage());
    }

    @ExceptionHandler(InvalidAgeException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidAge(InvalidAgeException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, "Edad inválida", ex.getMessage());
    }

    @ExceptionHandler(
        {
            UserAlreadyRegisteredException.class,
            PurchaseAlreadyRegisteredException.class,
            SectionAlreadyRegisteredException.class,
            PaymentAlreadyUsedException.class
        }
    )
    public ResponseEntity<Map<String, Object>> handleConflict(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Conflicto en la solicitud", ex.getMessage());
    }
}
